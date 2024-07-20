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
 * Yesterday,
 */
public class SimpleRAG {

    public static void main(String[] args) {
        SimpleAgent agent = createSimpleAgent();
        try(Scanner scanner = new Scanner(System.in)){
            while (true){
                Logger.info("User: ");
                String userQuery = scanner.nextLine();
                
                if("quit".equalsIgnoreCase(userQuery)){
                    Logger.info("you quit the program.");
                    break;
                }
                
                String agentAnswer = agent.answer(userQuery);
                
                Logger.info("Agent: " + agentAnswer);
            }
        }
    }

    private static Path getFilePath(String fileName){
        try{
            URL fileUrl = SimpleRAG.class.getClassLoader().getResource(fileName);
            if (fileUrl == null) {
                Logger.info("File not found in resources.");
            }
            Logger.debug("file path is: " + fileUrl.toURI().toString());
            return Paths.get(fileUrl.toURI());
            
        } catch (URISyntaxException e) {
            Logger.error("error: ", e);
            throw new RuntimeException(e);
        }
    }
    
    private static SimpleAgent createSimpleAgent() {
        Logger.debug(System.getenv("OPEN_AI_API_KEY"));
        //System.getenv("OPEN_AI_API_KEY")
        ChatLanguageModel chatModel = OpenAiChatModel.builder().apiKey(System.getenv("OPEN_AI_API_KEY")).modelName(GPT_4_O).build();
        
        Path documentPath = getFilePath("OpenAI launches GPT-4o mini, which will replace GPT-3.5 in ChatGPT  Ars Technica.md");
        Logger.info("Path: " + documentPath);
        DocumentParser documentParser = new TextDocumentParser();
        Document document = FileSystemDocumentLoader.loadDocument(documentPath, documentParser);
        
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        List<TextSegment> segments = splitter.split(document);
        
        /**
         * AllMiniLmL6V2EmbeddingModel, this model uses the BERT model which is a popular embedding model.
         */
//        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        
        //use OpenAI Embedding Model, model name is text-embedding-ada-002
        //baseUrl: https://api.openai.com/v1
        EmbeddingModel embeddingModel = useOpenAIEmbeddingModel();
        
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
            .embeddingStore(embeddingStore)
            .embeddingModel(embeddingModel)
            .maxResults(10)
            .minScore(0.7)
            .build();

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        
        return AiServices.builder(SimpleAgent.class).chatLanguageModel(chatModel).contentRetriever(contentRetriever).chatMemory(chatMemory).build();
    }
    
    private static EmbeddingModel useOpenAIEmbeddingModel(){
        return OpenAiEmbeddingModel.withApiKey(System.getenv("OPEN_AI_API_KEY"));
    }
    
    interface SimpleAgent{
        String answer(String query);
    }
}
