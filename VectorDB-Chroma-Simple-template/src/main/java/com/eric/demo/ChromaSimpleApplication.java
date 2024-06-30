package com.eric.demo;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
// import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import org.tinylog.Logger;
import static dev.langchain4j.internal.Utils.randomUUID;
import java.util.List;


// At first, you should run a chromadb Docker image. You can use the following command: docker run -p 8000:8000 chromadb/chroma
public class ChromaSimpleApplication {

	private static String collectionName = "chromadb" + randomUUID();

	public static void main(String[] args) {
		EmbeddingStore<TextSegment> embeddingStore = ChromaEmbeddingStore.builder()
				.baseUrl("http://localhost:8000")
				.collectionName(collectionName).build();

		// Please apply an OPENAI-API-KEY from OpenAI platform, then replace the "API-KEY"
		// EmbeddingModel embeddingModel =
		// OpenAiEmbeddingModel.builder().apiKey("API-KEY").modelName("text-embedding-ada-002").build();

		EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

		TextSegment segmentA = TextSegment.from("I like Montreal.");
		Embedding embeddingA = embeddingModel.embed(segmentA).content();
		embeddingStore.add(embeddingA, segmentA);

		TextSegment segmentB = TextSegment.from("I like Pizza.");
		Embedding embeddingB = embeddingModel.embed(segmentB).content();
		embeddingStore.add(embeddingB, segmentB);

		Embedding queryEmbedding = embeddingModel.embed("What is your favourite food?").content();
		@SuppressWarnings("deprecation")
		List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(queryEmbedding, 1);
		EmbeddingMatch<TextSegment> embeddingMatch = relevant.get(0);

		Logger.info("score: " + embeddingMatch.score());
		Logger.info("text: " + embeddingMatch.embedded().text());

	}

}
