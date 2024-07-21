package com.eric.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import static java.time.Duration.ofSeconds;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import org.tinylog.Logger;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O;


/**
 * The SimpleRAG class demonstrates a simple Retrieval-Augmented Generation (RAG) system.
 * It utilizes various components from the dev.langchain4j library to interact with documents,
 * embeddings, and AI models for processing and generating responses based on user input.
 * This example showcases the integration of different services and models to create a chat agent.
 */
public class SimpleRAG {
    /**
     * The main method serves as the entry point for the application.
     * It creates a SimpleAgent instance and enters a loop to process user input.
     * The application logs user queries and checks for a "quit" command to exit.
     *
     * @param args Command line arguments passed to the program (not used).
     */
    public static void main(String[] args) {
        // Creation of a SimpleAgent instance.
        SimpleAgent agent = createSimpleAgent();

        try(Scanner scanner = new Scanner(System.in)){
            while (true){
                // Prompting the user for input.
                Logger.info("User: ");
                
                // Reading user input from the console.
                String userQuery = scanner.nextLine();

                // Checking if the user wants to quit the application.
                if("quit".equalsIgnoreCase(userQuery)){
                    Logger.info("you quit the program.");
                    break;
                }
                
                String agentAnswer = agent.answer(userQuery);
                
                Logger.info("Agent: " + agentAnswer);
            }
        }
    }

    /**
     * Retrieves the file path for a given file name located in the resources directory.
     * This method uses the class loader to find the resource and converts the URL to a Path.
     *
     * @param fileName The name of the file to find.
     * @return Path to the file in the resources directory.
     * @throws RuntimeException if the URL to the file cannot be converted to a URI.
     */
    private static Path getFilePath(String fileName){
        try{
            // Attempt to get the URL of the file from the resources folder.
            URL fileUrl = SimpleRAG.class.getClassLoader().getResource(fileName);
            if (fileUrl == null) {
                // Log a message if the file is not found.
                Logger.info("File not found in resources.");
            }
            // Log the file path for debugging purposes.
            Logger.debug("file path is: " + fileUrl.toURI().toString());
            // Convert the URL to a Path and return it.
            return Paths.get(fileUrl.toURI());
            
        } catch (URISyntaxException e) {
            // Log the error and throw a RuntimeException if the URL cannot be converted to a URI.
            Logger.error("error: ", e);
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Creates and configures a SimpleAgent instance.
     * This includes setting up the chat model, loading and parsing a document,
     * splitting the document into segments, embedding these segments, and
     * initializing the content retriever with an embedding store.
     *
     * @return A configured SimpleAgent instance.
     */
    private static SimpleAgent createSimpleAgent() {
        // Log the OpenAI API key for debugging purposes.
        Logger.debug(System.getenv("OPEN_AI_API_KEY"));
    
        // Initialize the chat model with the OpenAI API key and model name.
        ChatLanguageModel chatModel = OpenAiChatModel.builder().apiKey(System.getenv("OPEN_AI_API_KEY")).modelName(GPT_4_O).build();
        
        // Retrieve the path to the document to be processed.
        Path documentPath = getFilePath("OpenAI launches GPT-4o mini, which will replace GPT-3.5 in ChatGPT  Ars Technica.md");
        Logger.info("Path: " + documentPath);

        // Load and parse the document from the file system.
        DocumentParser documentParser = new TextDocumentParser();
        Document document = FileSystemDocumentLoader.loadDocument(documentPath, documentParser);
        
        // Split the document into segments.
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        List<TextSegment> segments = splitter.split(document);
        
        /**
         * AllMiniLmL6V2EmbeddingModel, this model uses the BERT model which is a popular embedding model.
         */
//        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        
        //use OpenAI Embedding Model, model name is text-embedding-ada-002
        //baseUrl: https://api.openai.com/v1
        EmbeddingModel embeddingModel = useOpenAIEmbeddingModel();
        
        // Embed all segments of the document.
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
        
        // Initialize the embedding store and add all embeddings and their corresponding segments.
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);
        
        // Configure the content retriever with the embedding store and model, setting the maximum results and minimum score.
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
            .embeddingStore(embeddingStore)
            .embeddingModel(embeddingModel)
            .maxResults(10)
            .minScore(0.7)
            .build();

        // Initializes a chat memory with a maximum of 10 messages.
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        // Builds and returns a SimpleAgent instance configured with the chat model, content retriever, and chat memory.
        return AiServices.builder(SimpleAgent.class).chatLanguageModel(chatModel).contentRetriever(contentRetriever).chatMemory(chatMemory).build();
    }

    // Defines a method to initialize the OpenAI Embedding Model with an API key from the environment variables.
    private static EmbeddingModel useOpenAIEmbeddingModel(){
        return OpenAiEmbeddingModel.withApiKey(System.getenv("OPEN_AI_API_KEY"));
    }
    
    // Defines the SimpleAgent interface with a method for answering queries.
    interface SimpleAgent{
        String answer(String query);
    }
}
