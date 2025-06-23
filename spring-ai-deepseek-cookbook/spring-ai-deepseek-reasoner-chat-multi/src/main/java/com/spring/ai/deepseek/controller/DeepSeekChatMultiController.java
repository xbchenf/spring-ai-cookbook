package com.spring.ai.deepseek.controller;


import com.spring.ai.deepseek.vo.ReasoningResult;
import com.spring.ai.deepseek.vo.ChatRequest;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 基于 DeepSeek 推理模型（deepseek-reasoner）实现的控制器。
 * 提供单轮和多轮推理接口，并支持获取思维链（Chain of Thought）内容。
 *
 * @author 寻道AI小兵
 */
@RestController
@RequestMapping("/deepseek")
public class DeepSeekChatMultiController {

    private final DeepSeekChatModel chatModel;

    @Autowired
    public DeepSeekChatMultiController(DeepSeekChatModel chatModel) {
        this.chatModel = chatModel;
    }


    /**
     * 动态接收用户输入的 Prompt，并返回推理结果（单轮对话）
     *
     * @param message 包含用户输入的消息
     * @return 包含最终答案和 CoT 内容的结构化结果
     */
    @GetMapping("/ai/chat")
    public ReasoningResult chatWithDynamicPrompt(@RequestParam String message) {
        // 构建 Prompt
        DeepSeekChatOptions options = buildDefaultOptions();
        Prompt prompt = new Prompt(message, options);

        // 调用模型
        ChatResponse response = chatModel.call(prompt);

        // 提取响应内容
        DeepSeekAssistantMessage assistantMessage = (DeepSeekAssistantMessage) response.getResult().getOutput();
        String answer = assistantMessage.getText();
        String reasoning = assistantMessage.getReasoningContent();

        return new ReasoningResult(answer, reasoning);
    }

    /**
     * 构建默认的 DeepSeek 模型配置
     */
    private DeepSeekChatOptions buildDefaultOptions() {
        return DeepSeekChatOptions.builder()
                .model(DeepSeekApi.ChatModel.DEEPSEEK_REASONER.getValue())
                .build();
    }
    /**
     * 用于缓存每个会话的历史消息（Key: sessionId）
     */
    private final Map<String, List<Message>> sessionContexts = new ConcurrentHashMap<>();
    /**
     * 获取指定会话ID对应的上下文消息列表
     *
     * @param sessionId 会话ID
     * @return 对应的上下文消息列表
     */
    private List<Message> getSessionMessages(String sessionId) {
        return sessionContexts.computeIfAbsent(sessionId, k -> new ArrayList<>());
    }
    /**
     * 支持多轮对话的推理接口，接收用户输入并返回包含 CoT 的结构化结果
     *
     * @param message 包含用户输入的消息
     * @param sessionId 会话ID（可通过 Header 或 Cookie 传递）
     * @return 包含最终答案和 CoT 内容的结构化结果
     */
    @GetMapping("/ai/chat-multi")
    public ReasoningResult chatWithMultiRound(
            @RequestParam String message,
            @RequestParam String sessionId) {

        List<Message> messages = getSessionMessages(sessionId);

        // 添加用户消息到上下文
        messages.add(new UserMessage(message));

        // 构建 Prompt 并调用模型
        DeepSeekChatOptions options = buildDefaultOptions();
        Prompt prompt = new Prompt(messages, options);
        ChatResponse response = chatModel.call(prompt);

        // 提取响应内容
        DeepSeekAssistantMessage assistantMessage = (DeepSeekAssistantMessage) response.getResult().getOutput();
        String answer = assistantMessage.getText();
        String reasoning = assistantMessage.getReasoningContent();

        // 添加助手回复到上下文
        messages.add(new AssistantMessage(answer));

        // 返回结构化结果
        return new ReasoningResult(answer, reasoning);
    }

}