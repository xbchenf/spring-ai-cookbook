package spring.ai.mcp.weather.client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Application 是 Spring Boot 的主启动类。
 * 该类负责初始化 Spring Boot 应用程序，并配置相关的 MCP 工具回调和聊天客户端。
 *
 * @author 小兵寻道AI
 * @since 2025-5-28 10:20:14
 */
@SpringBootApplication
public class Application {

	/**
	 * 主方法，用于启动 Spring Boot 应用程序。
	 *
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * 注入用户输入的提示内容。
	 * 通过 application.yml 配置文件中的 ai.user.input 属性设置。
	 */
	@Value("${ai.user.input}")
	private String userInput;

	/**
	 * 定义一个 CommandLineRunner Bean，用于在应用启动后执行预定义的提问逻辑。
	 *
	 * @param chatClientBuilder ChatClient 构建器，用于创建聊天客户端实例
	 * @param tools 工具回调提供者，用于注册和调用工具函数
	 * @param context Spring 应用上下文，用于管理 Bean 生命周期
	 * @return CommandLineRunner 返回一个可执行的命令行任务
	 */
	@Bean
	public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools,
												 ConfigurableApplicationContext context) {

		return args -> {

			// 创建 ChatClient 实例，设置默认的工具回调
			var chatClient = chatClientBuilder
					.defaultToolCallbacks(tools)
					.build();

			// 输出当前的问题
			System.out.println("\n>>> QUESTION: " + userInput);

			// 发送问题给 AI 模型并获取回答
			System.out.println("\n>>> ASSISTANT: " + chatClient.prompt(userInput).call().content());

			// 关闭 Spring 应用上下文
			context.close();
		};
	}
}
