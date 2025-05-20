package com.spring.ai.deepseek.controller;


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
 *使用 ChatModel API 对话示例
 *
 * 专注于 OpenAI 模型的对话控制器（使用 OpenAiChatModel，功能简单）
 */
@RestController
@RequestMapping("/model")
public class ChatModelController {

    private final OpenAiChatModel chatModel;

    @Autowired
    public ChatModelController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/chat")
    public Map chat(@RequestParam(value = "message", defaultValue = "给我讲一个笑话") String message) {
        return Map.of("content", this.chatModel.call(message));
    }

    @GetMapping("/chat/stream")
    public Flux<ChatResponse> chatStream(@RequestParam(value = "message", defaultValue = "给我讲一个笑话") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt);
    }
}