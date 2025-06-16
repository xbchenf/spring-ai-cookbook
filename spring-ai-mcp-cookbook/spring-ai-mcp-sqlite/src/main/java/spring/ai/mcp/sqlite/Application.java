package spring.ai.mcp.sqlite;

import java.time.Duration;
import java.util.List;

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
 * @since 2025-6-15 15:55:19
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder,
			List<McpSyncClient> mcpClients, ConfigurableApplicationContext context) {

		return args -> {
			var chatClient = chatClientBuilder
					.defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpClients))
					.build();
			/*System.out.println("Running predefined questions with AI model responses:\n");

			// Question 1
			String question1 = "Can you connect to my SQLite database and tell me what products are available, and their prices?";
			System.out.println("QUESTION: " + question1);
			System.out.println("ASSISTANT: " + chatClient.prompt(question1).call().content());

			// Question 2
			String question2 = "What's the average price of all products in the database?";
			System.out.println("\nQUESTION: " + question2);
			System.out.println("ASSISTANT: " + chatClient.prompt(question2).call().content());

			// Question 3
			String question3 = "Can you analyze the price distribution and suggest any pricing optimizations?";
			System.out.println("\nQUESTION: " + question3);
			System.out.println("ASSISTANT: " + chatClient.prompt(question3).call().content());

			// Question 4
			String question4 = "Could you help me design and create a new table for storing customer orders?";
			System.out.println("\nQUESTION: " + question4);
			System.out.println("ASSISTANT: " + chatClient.prompt(question4).call().content());

			System.out.println("\nPredefined questions completed. Exiting application.");

			context.close();*/

			System.out.println("正在运行预定义问题并获取 AI 模型的回答：\n");

			// 问题 1
			String question1 = "你能连接到我的 SQLite 数据库，并告诉我有哪些产品以及它们的价格吗？";
			System.out.println("问题: " + question1);
			System.out.println("助手: " + chatClient.prompt(question1).call().content());

			// 问题 2
			String question2 = "数据库中所有产品的平均价格是多少？";
			System.out.println("\n问题: " + question2);
			System.out.println("助手: " + chatClient.prompt(question2).call().content());

			// 问题 3
			String question3 = "你能分析价格分布并建议一些定价优化方案吗？";
			System.out.println("\n问题: " + question3);
			System.out.println("助手: " + chatClient.prompt(question3).call().content());

			// 问题 4
			String question4 = "你能帮我设计并创建一个用于存储客户订单的新表吗？";
			System.out.println("\n问题: " + question4);
			System.out.println("助手: " + chatClient.prompt(question4).call().content());

			System.out.println("\n预定义问题已完成。退出应用程序。");
			context.close();

		};
	}

	@Bean(destroyMethod = "close")
	public McpSyncClient mcpClient() {

		var stdioParams = ServerParameters.builder("uvx")
				.args("mcp-server-sqlite", "--db-path",
						getDbPath())
				.build();

		var mcpClient = McpClient.sync(new StdioClientTransport(stdioParams))
				.requestTimeout(Duration.ofSeconds(10)).build();

		var init = mcpClient.initialize();

		System.out.println("MCP Initialized: " + init);

		return mcpClient;

	}

	private static String getDbPath() {
		//return Paths.get(System.getProperty("user.dir"), "test.db").toString();
		return "D:\\github\\spring-ai-cookbook\\spring-ai-mcp-cookbook\\spring-ai-mcp-sqlite\\test.db";
	}

}