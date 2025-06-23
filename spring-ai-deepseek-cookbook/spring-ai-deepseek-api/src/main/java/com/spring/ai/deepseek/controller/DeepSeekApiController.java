package com.spring.ai.deepseek.controller;

import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * DeepSeek API 示例
 */
@RestController
@RequestMapping("/deepSeekApi")
public class DeepSeekApiController {

    private final DeepSeekApi deepSeekApi;

    public DeepSeekApiController() {
        String apiKey = System.getenv("DEEPSEEK_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("DEEPSEEK_API_KEY 环境变量未设置");
        }
        this.deepSeekApi = DeepSeekApi.builder().apiKey(apiKey).build();
    }

    @GetMapping("/ai/generate")
    public ResponseEntity<DeepSeekApi.ChatCompletion> generate(
            @RequestParam(value = "message", defaultValue = "给我写一首现代诗") String message) {

        DeepSeekApi.ChatCompletionMessage chatCompletionMessage =
                new DeepSeekApi.ChatCompletionMessage(message, DeepSeekApi.ChatCompletionMessage.Role.USER);

        // Sync request
        ResponseEntity<DeepSeekApi.ChatCompletion> response = deepSeekApi.chatCompletionEntity(
                new DeepSeekApi.ChatCompletionRequest(List.of(chatCompletionMessage),
                        DeepSeekApi.ChatModel.DEEPSEEK_CHAT.getValue(), 0.8, false));

        return response;
    }

    @GetMapping("/ai/generateStream")
    public Flux<DeepSeekApi.ChatCompletionChunk> generateStream(
            @RequestParam(value = "message", defaultValue = "给我写一首现代诗") String message) {

        DeepSeekApi.ChatCompletionMessage chatCompletionMessage =
                new DeepSeekApi.ChatCompletionMessage(message, DeepSeekApi.ChatCompletionMessage.Role.USER);

        // Streaming request
        return deepSeekApi.chatCompletionStream(
                new DeepSeekApi.ChatCompletionRequest(List.of(chatCompletionMessage),
                        DeepSeekApi.ChatModel.DEEPSEEK_CHAT.getValue(), 0.8, true));
    }
}
