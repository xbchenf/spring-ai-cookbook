package com.spring.ai.ollama;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.DefaultToolCallingManager;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 寻道AI小兵
 * @since 2025-6-23
 */
@RestController
@RequestMapping("/ollama")
public class ToolCallingController {

    private static final Logger logger = LoggerFactory.getLogger(ToolCallingController.class);

    @Autowired
    private OllamaChatModel chatModel;

    /**
     * 处理带有工具调用能力的聊天请求
     *
     * @param userQuestion 用户输入的问题
     * @return 包含对话历史和回答的结构化响应
     */
    @GetMapping("/toolCalling")
    public Map<String, Object> ToolCallTest(@RequestParam("question") String userQuestion) {
        Map<String, Object> response = new HashMap<>();
        String conversationId = UUID.randomUUID().toString();

        try {
            ToolCallingManager toolCallingManager = DefaultToolCallingManager.builder().build();
            ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

            // 构建初始系统消息 + 用户问题
            ChatOptions chatOptions = ToolCallingChatOptions.builder()
                    .toolCallbacks(ToolCallbacks.from(new MathTools()))
                    .internalToolExecutionEnabled(false)
                    .build();

            Prompt initialPrompt = new Prompt(
                    List.of(new SystemMessage("You are a helpful assistant."), new UserMessage(userQuestion)),
                    chatOptions);

            chatMemory.add(conversationId, initialPrompt.getInstructions());

            // 第一次调用模型
            Prompt promptWithMemory = new Prompt(chatMemory.get(conversationId), chatOptions);
            ChatResponse chatResponse = this.chatModel.call(promptWithMemory);
            chatMemory.add(conversationId, chatResponse.getResult().getOutput());
            logger.info("模型首次回复: {}", chatResponse.getResult().getOutput().getText());

            // 循环处理工具调用
            while (chatResponse.hasToolCalls()) {
                ToolExecutionResult result = toolCallingManager.executeToolCalls(promptWithMemory, chatResponse);
                chatMemory.add(conversationId, result.conversationHistory().get(result.conversationHistory().size() - 1));
                promptWithMemory = new Prompt(chatMemory.get(conversationId), chatOptions);
                chatResponse = this.chatModel.call(promptWithMemory);
                chatMemory.add(conversationId, chatResponse.getResult().getOutput());
                logger.info("工具调用后模型回复: {}", chatResponse.getResult().getOutput().getText());
            }

            // 构造响应数据
            response.put("status", "success");
            response.put("conversationId", conversationId);
            response.put("userQuestion", userQuestion);
            response.put("aiAnswer", chatResponse.getResult().getOutput().getText());
            response.put("history", getHistoryAsList(chatMemory, conversationId));

        } catch (Exception e) {
            logger.error("处理带工具调用的聊天请求时发生错误", e);
            response.put("status", "error");
            response.put("message", "内部服务器错误，请稍后再试。");
        }

        return response;
    }

    /**
     * 将聊天记录转换为结构化的历史列表
     */
    private List<Map<String, String>> getHistoryAsList(ChatMemory chatMemory, String conversationId) {
        return chatMemory.get(conversationId).stream()
                .map(message -> {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("role", message.getMessageType().name());
                    messageMap.put("content", message.getText());
                    return messageMap;
                })
                .toList();
    }

    // 示例工具类
    static class MathTools {

        @Tool(description = "Multiply the two numbers")
        double multiply(double a, double b) {
            return a * b;
        }
    }

    record CountryInfo(@JsonProperty(required = true) String name,
                       @JsonProperty(required = true) String capital,
                       @JsonProperty(required = true) List<String> languages) {}

    record ActorsFilmsRecord(String actor, List<String> movies) {}
}
