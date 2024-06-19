package com.eric.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.service.AiServices;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Value("put your OPENAI API KEY AT HERE")
	private String OPENAI_API_KEY;

	// Uncomment to run
	@Bean("chatApplicationRunner")
	ApplicationRunner applicationRunner() {
		return args -> {
			ChatLanguageModel model = OpenAiChatModel.builder()
					.apiKey(OPENAI_API_KEY)
					.modelName(OpenAiChatModelName.GPT_3_5_TURBO)
					/*
					 * .logRequests(true)
					 * .logResponses(true)
					 * .maxRetries(3)
					 * .timeout(Duration.ofMillis(10000))
					 */
					.temperature(0.9)
					.maxTokens(100)
					.user("Test-user")
					.build();

			MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
					.maxMessages(20)
					.build();

			interface ConversationService {
				String chat(String message);
			}

			ConversationService conversation = AiServices.builder(ConversationService.class)
					.chatLanguageModel(model)
					.chatMemory(chatMemory)
					.build();

			List.of(
					"Hello!",
					"What is Machine Learning?",
					"Give me some dev tools or SDK name for ML development?").forEach(message -> {
						System.out.println("\nUser: " + message);
						System.out.println("LLM: " + conversation.chat(message));
					});
		};
	}

}
