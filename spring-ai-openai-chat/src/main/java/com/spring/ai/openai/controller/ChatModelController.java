package com.spring.ai.openai.controller;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * 控制器类，用于处理与聊天相关的请求。
 * 使用 OpenAiChatModel API 进行对话，专注于 OpenAI 模型。
 *
 * @author 寻道AI小兵
 * @since 2025-4-3 15:53:23
 *
 * 主要功能：
 * - 提供两个端点来处理不同类型的聊天请求。
 */
@RestController
@RequestMapping("/model")
public class ChatModelController {

    // OpenAiChatModel 实例，用于处理聊天请求
    private final OpenAiChatModel chatModel;

    /**
     * 构造函数，初始化 OpenAiChatModel
     *
     * @param chatModel OpenAiChatModel 实例
     */
    @Autowired
    public ChatModelController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 基本对话接口。
     * 使用默认消息进行对话，并返回 AI 的回复。
     *
     * @param message 用户输入的消息，默认为 "给我讲一个笑话"
     * @return 包含 AI 回复内容的 Map
     */
    @GetMapping("/chat")
    public Map<String, String> chat(@RequestParam(value = "message", defaultValue = "给我讲一个笑话") String message) {
        return Map.of("content", this.chatModel.call(message));
    }

    /**
     * 流式对话接口。
     * 以流的形式返回 AI 的回复，适用于需要实时响应的场景。
     *
     * @param message 用户输入的消息，默认为 "给我讲一个笑话"
     * @return AI生成的回复流
     */
    @GetMapping("/chat/stream")
    public Flux<ChatResponse> chatStream(@RequestParam(value = "message", defaultValue = "给我讲一个笑话") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt);
    }

}
