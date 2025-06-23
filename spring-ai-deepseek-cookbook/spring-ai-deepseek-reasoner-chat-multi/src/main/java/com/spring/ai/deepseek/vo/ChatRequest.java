package com.spring.ai.deepseek.vo;


/**
 * 封装客户端发送的聊天请求
 */
public class ChatRequest {

    public ChatRequest(String userMessage) {
        this.userMessage = userMessage;
    }

    /**
     * 用户输入的提示词内容
     */
    private String userMessage;

    public ChatRequest() {
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
