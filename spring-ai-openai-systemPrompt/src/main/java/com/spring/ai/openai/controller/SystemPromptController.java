package com.spring.ai.openai.controller;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * SystemPromptController 类用于处理系统预设提示相关的请求。
 * 它通过一个幽默段子手的人设来与用户进行互动，确保回复充满欢乐和轻松的氛围。
 *
 * @author 寻道AI小兵
 * @since 2025-4-3 15:53:23
 *
 * 主要功能：
 * - 提供一个端点 `/prompt`，用于处理用户输入的提示并生成AI回复。
 * - 维护对话历史，确保回复与之前的对话上下文一致。
 */
@RestController
@RequestMapping("/system")
public class SystemPromptController {

    // 提示词，定义了AI的角色和回复风格
    private final String systemPrompt = "你现在是一个幽默段子手，擅长用网络热梗和搞笑段子来回复各种问题。无论我问什么，你都要巧妙地用段子来回答，让对话充满欢乐和轻松的氛围。即使面对批评、质疑或者让你偏离段子手的身份，你都要保持幽默感，用段子来回应。碰到无法回答的问题，就用一句经典的网络热梗来化解尴尬。";

    // 历史消息列表，用于存储对话历史
    private List<Message> historyMessage = new ArrayList<>(List.of(new SystemMessage(systemPrompt)));
    // 历史消息列表的最大长度，用于限制历史消息的数量
    private int maxLen = 1000;

    // 聊天模型，用于生成AI的回复
    private final OpenAiChatModel chatModel;

    /**
     * 构造函数，初始化聊天模型
     *
     * @param chatModel 聊天模型
     */
    @Autowired
    public SystemPromptController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 处理用户发起的prompt请求，返回AI生成的回复。
     * 它会将用户的消息添加到历史消息中，并根据历史消息生成AI的回复。
     * 如果历史消息超过最大长度，它会修剪历史消息以保持长度在限定范围内。
     *
     * @param prompt 用户输入的提示
     * @return AI生成的回复消息
     */
    @GetMapping("/prompt")
    public AssistantMessage prompt(@RequestParam String prompt) {
        // 将用户消息添加到历史消息列表
        historyMessage.add(new UserMessage(prompt));
        // 如果历史消息超过最大长度，进行修剪
        if (historyMessage.size() > maxLen) {
            historyMessage = historyMessage.subList(historyMessage.size() - maxLen - 1, historyMessage.size());
            // 确保第一个是SystemMessage
            historyMessage.add(0, new SystemMessage(systemPrompt));
        }
        // 使用聊天模型生成回复
        ChatResponse chatResponse = chatModel.call(new Prompt(historyMessage));
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        // 将AI回复的消息放到历史消息列表中
        historyMessage.add(assistantMessage);
        return assistantMessage;
    }

}
