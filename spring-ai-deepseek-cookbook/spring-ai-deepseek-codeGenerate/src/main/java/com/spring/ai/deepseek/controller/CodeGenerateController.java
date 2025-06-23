package com.spring.ai.deepseek.controller;


import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 以下是用于聊天前缀补全的完整 Java 代码示例。
 * 在这个示例中，我们将助手的 prefix 消息设置为"```java\n"，以强制模型输出 java 代码，
 * 并将 stop 参数设置为['`']，以防止模型提供额外的解释。
 */
@RestController
@RequestMapping("/code")
public class CodeGenerateController {

    private final DeepSeekChatModel chatModel;

    @Autowired
    public CodeGenerateController(DeepSeekChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/generateJavaCode")
    public String generate(@RequestParam(value = "message", defaultValue = "Please write quick sort code") String message) {
        UserMessage userMessage = new UserMessage(message);
        Message assistantMessage = DeepSeekAssistantMessage.prefixAssistantMessage("```java\n");
        Prompt prompt = new Prompt(List.of(userMessage, assistantMessage), ChatOptions.builder().stopSequences(List.of("```\n")).build());
        ChatResponse response = chatModel.call(prompt);
        System.out.println("response------------: " + response); // 添加调试信息
        return response.getResult().getOutput().getText();
    }
}