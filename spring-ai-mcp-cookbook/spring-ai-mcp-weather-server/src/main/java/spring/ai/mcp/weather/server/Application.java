package spring.ai.mcp.weather.server;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Application 是 Spring Boot 的主启动类。
 * 该类负责初始化 Spring Boot 应用程序，并配置相关的工具回调（Tool Callback）。
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
	 * 配置 WeatherService 的工具回调提供者（ToolCallbackProvider）。
	 * 使用 MethodToolCallbackProvider 构建器将 WeatherService 注册为一个工具对象，
	 * 这样它就可以通过 Spring AI 的工具调用机制被调用。
	 *
	 * @param weatherService 天气服务实例
	 * @return ToolCallbackProvider 返回一个包含天气服务的工具回调提供者
	 */
	@Bean
	public ToolCallbackProvider weatherTools(WeatherService weatherService) {
		return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
	}

	/**
	 * 定义一个简单的输入数据模型，用于封装文本输入。
	 * 该记录类（record）是不可变的，用于传递输入字符串。
	 *
	 * @param input 输入的文本内容
	 */
	public record TextInput(String input) {
	}

	/**
	 * 定义一个名为 "toUpperCase" 的函数工具回调（FunctionToolCallback）。
	 * 该工具接收一个 TextInput 对象，并返回其大写形式。
	 * 该工具可以被 Spring AI 调用，作为功能调用的一部分。
	 *
	 * @return ToolCallback 返回一个构建好的函数工具回调
	 */
	@Bean
	public ToolCallback toUpperCase() {
		return FunctionToolCallback.builder("toUpperCase", (TextInput input) -> input.input().toUpperCase())
			.inputType(TextInput.class)
			.description("Put the text to upper case")
			.build();
	}

}
