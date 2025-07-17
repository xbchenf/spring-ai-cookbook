package com.spring.ai.ollama;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 寻道AI小兵
 * @since 2025-6-23
 * 系统提示词相关接口，用于测试不同场景下的模型响应
 */
@RestController
@RequestMapping("/ollama")
public class SystemPromptController {

    @Autowired
    private OllamaChatModel chatModel;

    /**
     * 带系统角色的聊天测试接口
     * 模拟一个带有个性化设定的AI助手，根据指定风格回答问题
     *
     * @return 模型生成的回答内容
     */
    @GetMapping("/systemRole")
    public String systemRoleTest() {
        Message systemMessage = new SystemPromptTemplate("""
				你是一个乐于助人的AI助手。你的名字是{name}。
				你是一个帮助人们寻找信息的AI助手。
				你的名字是{name}
				你应该用你的名字回复用户的请求，并以{voice}的风格进行回答。
				""").createMessage(Map.of("name", "小千", "voice", "脱口秀"));

        UserMessage userMessage = new UserMessage("请告诉我中国历史上最出名的五个暴君。");

        var portableOptions = ChatOptions.builder().temperature(0.7).build();

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), portableOptions);

        ChatResponse response = this.chatModel.call(prompt);
        System.out.println("系统角色测试 - 模型响应内容: " + response.getResult().getOutput().getText());

        return response.getResult().getOutput().getText();
    }

}
