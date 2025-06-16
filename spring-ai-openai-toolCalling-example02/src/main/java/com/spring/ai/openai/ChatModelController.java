package com.spring.ai.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ChatModelController类用于处理与聊天模型相关的HTTP请求
 * 它利用了Spring AI的ChatModel和自定义的DateTimeTools来提供一个工具回调的功能
 *
 * @author 寻道AI小兵
 * @since 2025-5-21
 */
@RestController
@RequestMapping("/model")
class ChatModelController {

    // ChatModel实例，用于处理聊天相关的任务
    private final ChatModel chatModel;
    // DateTimeTools实例，用于处理日期和时间相关的操作
    private final DateTimeTools dateTimeTools;

    /**
     * 构造函数，初始化ChatModelController实例
     *
     * @param chatModel ChatModel实例，用于聊天交互
     */
    ChatModelController(ChatModel chatModel) {
        this.chatModel = chatModel;
        this.dateTimeTools = new DateTimeTools();
    }

    /**
     * 处理工具回调请求的端点
     * 该方法向ChatClient发送一个提示，要求设置一个从现在起3分钟后的闹钟，并返回ChatClient的响应内容
     *
     * @return ChatClient的响应内容，即闹钟设置的结果
     */
    @GetMapping("/toolCallBack")
    public String toolCallBack() {
        String response = ChatClient.create(chatModel)
                .prompt("Can you set an alarm 3 minutes from now?")
                .tools(new DateTimeTools())
                .call()
                .content();
        return response;
    }
}
