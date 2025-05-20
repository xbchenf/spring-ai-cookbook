package com.spring.ai.openai.controller;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制器类，用于处理带有上下文的聊天请求。
 * 使用 OpenAI 的聊天模型来生成回复，并维护对话历史以保持上下文。
 *
 * @author 寻道AI小兵
 * @since 2025-4-3 15:53:23
 *
 * 主要功能：
 * - 提供一个端点来处理带有上下文的聊天请求。
 */
@RestController
public class ContextChatController {

    // 历史消息列表，用于存储聊天的历史记录
    private static List<Message> historyMessage = new ArrayList<>();
    // 历史消息列表的最大长度，用于限制历史记录的数量
    private static final int maxLen = 1000;

    // 聊天模型，用于生成AI的回复
    private final OpenAiChatModel chatModel;

    /**
     * 构造函数，初始化聊天模型
     *
     * @param chatModel 聊天模型
     */
    @Autowired
    public ContextChatController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 获取带有上下文的回复。
     * 此方法首先将用户输入添加到历史消息中，然后检查历史消息的长度。
     * 如果超过最大长度，则截取最近的消息。接着使用聊天模型生成回复，
     * 并将回复添加到历史消息中。
     *
     * @param prompt 用户输入的文本
     * @return AI生成的回复消息
     */
    @GetMapping("/context")
    public AssistantMessage context(@RequestParam String prompt) {
        // 用户输入的文本是UserMessage
        historyMessage.add(new UserMessage(prompt));
        // 发给AI前对历史消息列表的长度进行检查
        if (historyMessage.size() > maxLen) {
            historyMessage = historyMessage.subList(historyMessage.size() - maxLen - 1, historyMessage.size());
        }
        // 获取AssistantMessage
        ChatResponse chatResponse = chatModel.call(new Prompt(historyMessage));
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        // 将AI回复的消息放到历史消息列表中
        historyMessage.add(assistantMessage);
        return assistantMessage;
    }

}
