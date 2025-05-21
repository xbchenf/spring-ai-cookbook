package com.spring.ai.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器类，用于处理与 ChatModel 相关的 HTTP 请求。
 * 提供基于工具调用（如获取时间）的接口示例。
 *
 * @author 寻道AI小兵
 * @since 2025-5-21
 */
@RestController
@RequestMapping("/model")
class ChatModelController {

    /**
     * ChatModel 实例，用于与 AI 模型交互。
     */
    private final ChatModel chatModel;

    /**
     * DateTimeTools 实例，提供时间相关功能的工具类。
     */
    private final DateTimeTools dateTimeTools;

    /**
     * 构造函数，初始化 ChatModel 和 DateTimeTools。
     *
     * @param chatModel AI 模型服务实例
     */
    ChatModelController(ChatModel chatModel) {
        this.chatModel = chatModel;
        this.dateTimeTools = new DateTimeTools();
    }

    /**
     * 接口方法：调用 AI 模型并使用工具获取明天的日期。
     * 示例请求路径：GET /model/toolCallBack1
     *
     * @return AI 模型返回的响应内容
     */
    @GetMapping("/toolCallBack1")
    public String toolCallBack1() {
        String response = ChatClient.create(chatModel)
                .prompt("What day is tomorrow?")
                .tools(dateTimeTools)
                .call()
                .content();
        return response;
    }

    /**
     * 接口方法：仅调用 AI 模型，不使用任何工具。
     * 示例请求路径：GET /model/toolCallBack2
     *
     * @return AI 模型返回的响应内容
     */
    @GetMapping("/toolCallBack2")
    public String toolCallBack2() {
        String response = ChatClient.create(chatModel)
                .prompt("What day is tomorrow?")
                //.tools(dateTimeTools) // 工具调用已注释
                .call()
                .content();
        return response;
    }
}
