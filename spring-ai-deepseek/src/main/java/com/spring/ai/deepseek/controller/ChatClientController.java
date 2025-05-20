package com.spring.ai.deepseek.controller;

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
 * 使用 ChatClient API 对话示例
 *
 * 多提供商支持的对话控制器（使用 ChatClient，支持复杂选项配置）
 */
@RestController
@RequestMapping("/client")
class ChatClientController {

    private final ChatClient chatClient;

    // 配置化的模型和参数
    private static final String DEFAULT_MODEL = "deepseek-reasoner";
    private static final double DEFAULT_TEMPERATURE = 0.9;

    ChatClientController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * 基本对话接口
     */
    @GetMapping("/chat")
    String chat(@RequestParam String question) {
        return chatClient
                .prompt(question)
                .call()
                .content();
    }

    /**
     * 流式对话接口
     */
    @GetMapping("/chat/stream")
    Flux<String> chatStream(@RequestParam String question) {
        return chatClient.prompt(question).stream().content();
    }

    /**
     * 使用通用选项的对话接口
     * 如果你的应用需要支持多个对话 API 提供商(自定义model和temperature)，建议优先使用 chatGenericOptions 方法。
     */
    @GetMapping("/chat/generic-options")
    String chatGenericOptions(@RequestParam String question) {
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
     * 使用特定提供商选项的对话接口
     * 比如：你的应用专注于 OpenAI 提供商，并且需要利用其特有的功能（如 logprobs），则应选择 chatProviderOptions 方法。
     */
    @GetMapping("/chat/provider-options")
    String chatProviderOptions(@RequestParam String question) {
        return chatClient
                .prompt(question)
                .options(OpenAiChatOptions.builder()
                        .logprobs(true)
                        .build())
                .call()
                .content();
    }

}
