package com.spring.ai.deepseek;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 函数调用控制器
 *
 * 该控制器用于演示如何使用 DeepSeek 模型进行函数调用（工具调用）。
 * 提供了两种不同的函数调用方式：
 * - /function/tool/test1：直接注册并调用天气查询工具
 * - /function/tool/test2：通过工具名称调用已注册的天气查询工具
 *
 * @author 寻道AI小兵
 */
@RestController
@RequestMapping("/function/tool")
public class FunctionCallingController {
    private static final Logger logger = LoggerFactory.getLogger(FunctionCallingController.class);

    private final DeepSeekChatModel chatModel;

    /**
     * 构造函数注入 DeepSeekChatModel 实例
     *
     * @param chatModel Spring AI 提供的 DeepSeek 聊天模型实例
     */
    @Autowired
    public FunctionCallingController(DeepSeekChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 函数调用测试接口1 - 直接注册并调用工具
     *
     * 示例请求：GET /function/tool/test1
     *
     * 功能描述：
     * - 创建一个直接绑定到 WeatherService 的函数调用回调
     * - 构建包含工具调用选项的 ChatOptions
     * - 向 DeepSeek 模型发送带有工具调用能力的提示
     * - 返回模型处理后的文本响应
     *
     * @return 包含 Copenhagen 当前天气信息的文本响应
     */
    @GetMapping("/test1")
    public String functionCallingTest1() {

        // 创建函数调用回调，绑定到 WeatherService
        ToolCallback toolCallback = FunctionToolCallback
                .builder("currentWeather", new WeatherService())
                .description("Get the weather in location")
                .inputType(WeatherService.WeatherRequest.class)
                .build();

        // 构建包含工具调用选项的 ChatOptions
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(toolCallback)
                .build();

        // 创建包含提示和工具调用配置的 Prompt 对象
        Prompt prompt = new Prompt("What's the weather like in Copenhagen?", chatOptions);

        // 调用 DeepSeek 模型处理请求
        ChatResponse response = chatModel.call(prompt);

        // 返回模型处理后的文本响应
        return response.getResult().getOutput().getText();
    }

    /**
     * 函数调用测试接口2 - 通过工具名称调用已注册工具
     *
     * 示例请求：GET /function/tool/test2
     *
     * 功能描述：
     * - 使用已注册的工具名称构建工具调用选项
     * - 向 DeepSeek 模型发送带有工具调用能力的提示
     * - 返回模型处理后的文本响应
     *
     * 注意事项：
     * - 这种方式依赖于已在 Spring 上下文中注册的工具
     * - 在本示例中需要确保 WeatherTools 配置类已被正确加载
     *
     * @return 包含 Copenhagen 当前天气信息的文本响应
     */
    @GetMapping("/test2")
    public String functionCallingTest2() {

        // 使用已注册工具名称构建工具调用选项
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolNames("currentWeather")
                .build();

        // 创建包含提示和工具调用配置的 Prompt 对象
        Prompt prompt = new Prompt("What's the weather like in Copenhagen?", chatOptions);

        // 调用 DeepSeek 模型处理请求
        ChatResponse response = chatModel.call(prompt);

        // 返回模型处理后的文本响应
        return response.getResult().getOutput().getText();
    }
}
