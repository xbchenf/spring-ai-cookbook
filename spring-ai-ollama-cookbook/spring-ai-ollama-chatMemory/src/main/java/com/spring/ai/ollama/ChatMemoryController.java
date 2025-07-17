package com.spring.ai.ollama;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 聊天记忆控制器
 * 提供基于 MessageWindowChatMemory 的对话历史管理功能
 *
 * @author 寻道AI小兵
 * @since 2025-6-23
 */
@RestController
@RequestMapping("/ollama")
public class ChatMemoryController {

    private static final Logger logger = LoggerFactory.getLogger(ChatMemoryController.class);

    @Autowired
    private OllamaChatModel chatModel;

    // 全局共享的聊天记忆实例（模拟 session 级别的存储）
    private final ChatMemory globalChatMemory = new MessageWindowChatMemory();

    /**
     * 处理用户的聊天请求，支持动态输入并保留对话历史。
     *
     * @param userQuestion 用户输入的问题
     * @param conversationId 可选参数，用于关联之前的会话；若为空则新建会话
     * @return 包含会话ID、用户问题和AI回答的结构化响应
     */
    @GetMapping("/chat/memory")
    public Map<String, Object> MemoryChat(
            @RequestParam("question") String userQuestion,
            @RequestParam(name = "conversationId", required = false) String conversationId) {
        Map<String, Object> response = new HashMap<>();

        // 如果没有提供 conversationId，则生成一个新的
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = UUID.randomUUID().toString();
        }

        try {
            // 添加用户消息到记忆中
            this.globalChatMemory.add(conversationId, new UserMessage(userQuestion));

            // 构建 Prompt 并调用模型
            Prompt prompt = new Prompt(this.globalChatMemory.get(conversationId));
            ChatResponse chatResponse = this.chatModel.call(prompt);
            String answer = chatResponse.getResult().getOutput().getText();

            // 添加 AI 回答到记忆中
            this.globalChatMemory.add(conversationId, chatResponse.getResult().getOutput());

            // 构建结构化响应
            response.put("status", "success");
            response.put("conversationId", conversationId);
            response.put("userQuestion", userQuestion);
            response.put("aiAnswer", answer);

            logger.info("用户提问: {}, 模型回答: {}", userQuestion, answer);
        } catch (Exception e) {
            logger.error("处理聊天请求时发生错误", e);
            response.put("status", "error");
            response.put("message", "内部服务器错误，请稍后再试。");
        }

        return response;
    }
}
