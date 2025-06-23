package com.spring.ai.deepseek;

// 导入必要的类和接口
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * SystemPromptController 是一个 REST 控制器，用于处理与系统提示相关的请求。
 * 该控制器通过 SystemPromptTemplate 加载系统消息模板，并结合用户输入的消息生成响应。
 *
 * @author 寻道AI小兵
 */
@RestController
@RequestMapping("/role")
public class SystemPromptController {

    // 注入 DeepSeekChatModel 实例，用于调用 DeepSeek 的聊天模型
    private final DeepSeekChatModel chatModel;

    /**
     * 构造函数，用于初始化 DeepSeekChatModel 实例。
     *
     * @param chatModel 提供 DeepSeek 聊天功能的模型实例
     */
    @Autowired
    public SystemPromptController(DeepSeekChatModel chatModel) {
        this.chatModel = chatModel;
    }

    // 注入系统消息模板资源文件 system-message.st
    @Value("classpath:system-message.st")
    private Resource systemResource;

    /**
     * 处理 GET 请求 /role/chat，生成带有系统提示的聊天响应。
     *
     * @param message 用户输入的消息内容
     * @return 模型生成的响应文本
     */
    @GetMapping("/chat")
    public String roleTest(@RequestParam String message) {
        // 创建用户消息对象
        UserMessage userMessage = new UserMessage(message);

        // 使用系统资源文件创建系统提示模板
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(this.systemResource);

        // 根据模板生成系统消息，传入参数 name 和 voice
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", "小古", "voice", "古代文人"));

        // 创建包含系统消息和用户消息的提示对象
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        // 调用聊天模型生成响应
        ChatResponse response = this.chatModel.call(prompt);

        // 返回模型生成的响应文本
        return response.getResults().get(0).getOutput().getText();
    }

}
