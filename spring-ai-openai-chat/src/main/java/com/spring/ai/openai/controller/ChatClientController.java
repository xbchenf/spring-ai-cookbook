package com.spring.ai.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;

/**
 * 控制器类，用于处理与聊天相关的请求。
 * 使用 ChatClient API 进行对话，支持多提供商配置和复杂选项。
 *
 * @author 寻道AI小兵
 * @since 2025-4-3 15:53:23
 *
 * 主要功能：
 * - 提供多个端点来处理不同配置的聊天请求。
 */
@RestController
@RequestMapping("/client")
public class ChatClientController {

    // ChatClient 实例，用于处理聊天请求
    private final ChatClient chatClient;

    // 配置化的模型和参数
    private static final String DEFAULT_MODEL = OpenAiApi.ChatModel.GPT_4_TURBO.getValue();
    private static final double DEFAULT_TEMPERATURE = 0.9;

    /**
     * 构造函数，初始化 ChatClient
     *
     * @param chatClientBuilder ChatClient 构建器
     */
    public ChatClientController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * 基本对话接口。
     * 使用默认配置进行对话。
     *
     * @param question 用户输入的问题
     * @return AI生成的回复
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String question) {
        return chatClient
                .prompt(question)
                .call()
                .content();
    }

    /**
     * 使用通用选项的对话接口。
     * 如果你的应用需要支持多个对话 API 提供商（自定义 model 和 temperature），建议优先使用此方法。
     *
     * @param question 用户输入的问题
     * @return AI生成的回复
     */
    @GetMapping("/chat/generic-options")
    public String chatGenericOptions(@RequestParam String question) {
        return chatClient
                .prompt(question)
                .options(ChatOptions.builder()
                        .model(DEFAULT_MODEL)
                        .temperature(DEFAULT_TEMPERATURE)
                        .build())
                .call()
                .content();
    }

    /**
     * 使用特定提供商选项的对话接口。
     * 比如：你的应用专注于 OpenAI 提供商，并且需要利用其特有的功能（如 logprobs），则应选择此方法。
     *
     * @param question 用户输入的问题
     * @return AI生成的回复
     */
    @GetMapping("/chat/provider-options")
    public String chatProviderOptions(@RequestParam String question) {
        return chatClient
                .prompt(question)
                .options(OpenAiChatOptions.builder()
                        .logprobs(true)
                        .build())
                .call()
                .content();
    }

    /**
     * 流式对话接口。
     * 以流的形式返回 AI 的回复，适用于需要实时响应的场景。
     *
     * @param question 用户输入的问题
     * @return AI生成的回复流
     */
    @GetMapping("/chat/stream")
    public Flux<String> chatStream(@RequestParam String question) {
        return chatClient.prompt(question).stream().content();
    }

}
