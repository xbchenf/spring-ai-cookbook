package spring.ai.mcp;

import java.time.Duration;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author 小兵寻道AI
 * @since 2025-5-28 10:20:14
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder,
			McpSyncClient mcpClient, ConfigurableApplicationContext context) {

		return args -> {
			var chatClient = chatClientBuilder
					.defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpClient))
					.build();

			System.out.println("Running predefined questions with AI model responses:\n");

			// Question 1
			String question1 = "hello";
			System.out.println("QUESTION: " + question1);
			System.out.println("ASSISTANT: " + chatClient.prompt(question1).call().content());

			// Question 2
			String question2 = "请问目录 `D:\\nodejsProject` 下面有哪几个文件？";
			System.out.println("\nQUESTION: " + question2);
			System.out.println("ASSISTANT: " + chatClient.prompt(question2).call().content());

			context.close();

		};
	}

	@Bean(destroyMethod = "close")
	public McpSyncClient mcpClient() {

		// based on
		// https://github.com/modelcontextprotocol/servers/tree/main/src/filesystem
		var stdioParams = ServerParameters.builder("D:\\Program Files\\nodejs\\npx.cmd")
				.args("-y", "@modelcontextprotocol/server-filesystem", "D:\\nodejsProject")
				.build();

		var mcpClient = McpClient.sync(new StdioClientTransport(stdioParams))
				.requestTimeout(Duration.ofSeconds(10)).build();

		var init = mcpClient.initialize();

		System.out.println("MCP Initialized: " + init);

		return mcpClient;

	}

	/*private static String getDbPath() {
		return Paths.get(System.getProperty("user.dir"), "target").toString();
	}*/

}