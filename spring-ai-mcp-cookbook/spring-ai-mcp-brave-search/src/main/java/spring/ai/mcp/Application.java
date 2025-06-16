package spring.ai.mcp;

import java.util.List;

import io.modelcontextprotocol.client.McpSyncClient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}	

	@Bean
	public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpSyncClients,
			ConfigurableApplicationContext context) {

		return args -> {

			var chatClient = chatClientBuilder
					.defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients))
					.build();

			String question = "Does Spring AI support the Model Context Protocol? Please provide some references.";

			System.out.println("QUESTION: " + question);
			System.out.println("ASSISTANT: " + chatClient.prompt(question).call().content());

			context.close();
		};
	}
}