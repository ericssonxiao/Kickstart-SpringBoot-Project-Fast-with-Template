package com.eric.qdrant;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Collections;
import org.testcontainers.qdrant.QdrantContainer;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static dev.langchain4j.internal.Utils.randomUUID;


/**
 * A really simple way to run Qdrant vector database by testcontainer, a java library for integrating DB envronments and running the whole of testing.
 * 1. build EmbeddingStore
 * 2. create Qdrant client
 * 3. use all-minilm-l6-v2.onnx model, SentenceTransformers all-MiniLM-L6-v2 embedding model that runs within your Java application's process.
 * More details
 * <a href="https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2">here</a> and
 * <a href="https://www.sbert.net/docs/pretrained_models.html">here</a>
 * 4. create some TextSegments then add into Qdrant vector database.
 * 5. Represents a matched embedding along with its relevance score (derivative of Manhattan distance), ID, and original embedded content.
 * 
 */
public class QdrantSimpleExample {

  private static int grpcPort = 6334;
  private static String collectionName = "myqdrantdb-" + randomUUID();
  private static Collections.Distance distance = Collections.Distance.Manhattan;
  private static int dimension = 384;

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    try (QdrantContainer qdrant = new QdrantContainer("qdrant/qdrant:latest")) {

      qdrant.start();

      EmbeddingStore<TextSegment> embeddingStore = QdrantEmbeddingStore.builder()
                                                        .host(qdrant.getHost())
                                                        .port(qdrant.getMappedPort(grpcPort))
                                                        .collectionName(collectionName)
                                                        .build();

      QdrantClient client = new QdrantClient( 
                                QdrantGrpcClient.newBuilder(qdrant.getHost(), qdrant.getMappedPort(grpcPort), false)
                                .build());

      client.createCollectionAsync(collectionName, Collections.VectorParams.newBuilder().setDistance(distance).setSize(dimension).build()).get();

      EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

      TextSegment segment0 = TextSegment.from("I like dance.");
      Embedding embedding0 = embeddingModel.embed(segment0).content();
      embeddingStore.add(embedding0, segment0);

      TextSegment segment1 = TextSegment.from("I hate film.");
      Embedding embedding1 = embeddingModel.embed(segment1).content();
      embeddingStore.add(embedding1, segment1);

      TextSegment segment2 = TextSegment.from("Mr. Jack is awesome in the film.");
      Embedding embedding2 = embeddingModel.embed(segment2).content();
      embeddingStore.add(embedding2, segment2);

      TextSegment segment3 = TextSegment.from("I like reading");
      Embedding embedding3 = embeddingModel.embed(segment3).content();
      embeddingStore.add(embedding3, segment3);
      
      TextSegment segment4 = TextSegment.from("I hate dance and reading.");
      Embedding embedding4 = embeddingModel.embed(segment4).content();
      embeddingStore.add(embedding4, segment4);

      Embedding queryEmbedding = embeddingModel.embed("Did you ever like something?").content();
      List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(queryEmbedding, 1);
      System.out.println("size: " + relevant.size());
      EmbeddingMatch<TextSegment> embeddingMatch = relevant.get(0);

      System.out.println(embeddingMatch.score());
      System.out.println(embeddingMatch.embedded().text());
    }
  }
}
