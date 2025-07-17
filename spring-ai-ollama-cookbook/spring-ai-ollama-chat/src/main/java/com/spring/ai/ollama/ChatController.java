package com.spring.ai.ollama;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 寻道AI小兵
 * @since 2025-6-23
 * Ollama 聊天功能控制器，提供多种聊天交互方式
 */
@RestController
@RequestMapping("/ollama")
public class ChatController {

    @Autowired
    private OllamaChatModel chatModel;

    private static final String OLLAMA_MODEL = "llama3.1:8b";

    /**
     * 基础聊天接口，使用默认模型处理用户消息
     * @param message 用户输入的消息
     * @return 模型响应内容
     */
    @GetMapping("/chat")
    public String basicChat(@RequestParam(value = "message", defaultValue = "你是谁") String message) {
        ChatResponse response = chatModel.call(new Prompt(message));
        System.out.println("基础聊天 - 模型响应内容: " + response.getResult());
        return response.getResult().getOutput().getText();
    }

    /**
     * 指定模型的聊天接口，可选择模型并设置参数
     * @param message 用户输入的消息
     * @return 模型响应内容
     */
    @GetMapping("/chatMode")
    public String chatWithSpecificModel(@RequestParam(value = "message", defaultValue = "你是谁") String message) {
        ChatResponse response = chatModel.call(
                new Prompt(message,
                        OllamaOptions.builder()
                                .model("llama3.1:8b")
                                .temperature(0.4)
                                .build()
                ));
        System.out.println("指定模型聊天 - 模型响应内容: " + response.getResult());
        return response.getResult().getOutput().getText();
    }

    /**
     * 使用ChatClient的聊天接口，展示更灵活的调用方式
     * @param message 用户输入的消息
     * @return 模型响应内容
     */
    @GetMapping("/chatClient")
    public String chatUsingClient(@RequestParam(value = "message", defaultValue = "你是谁") String message) {
        String result = ChatClient.create(this.chatModel)
                .prompt(message)
                .options(OllamaOptions.builder().model(OLLAMA_MODEL).build())
                .call()
                .content();
        System.out.println("ChatClient聊天 - 模型响应内容: " + result);
        return result;
    }

}
