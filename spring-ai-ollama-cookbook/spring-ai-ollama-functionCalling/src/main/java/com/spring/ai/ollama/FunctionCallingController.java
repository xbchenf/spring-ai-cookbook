package com.spring.ai.ollama;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 寻道AI小兵
 * @since 2025-6-23
 */
@RestController
@RequestMapping("/ollama")
public class FunctionCallingController {

    private static final Logger logger = LoggerFactory.getLogger(FunctionCallingController.class);

    @Autowired
    private OllamaChatModel chatModel;



    /**
     * 处理带有工具调用能力的聊天请求
     *
     * @param userQuestion 用户输入的问题
     * @return 包含对话历史和回答的结构化响应
     */
    @GetMapping("/functionCalling")
    public String functionCallTest(@RequestParam("question") String userQuestion) {
        UserMessage userMessage = new UserMessage(userQuestion
                /*"查找北京、深圳、广州三个城市的天气情况，多少摄氏度？"*/);

        List<Message> messages = new ArrayList<>(List.of(userMessage));

        var promptOptions = OllamaOptions.builder()
              //  .model(MODEL)
                .toolCallbacks(List.of(FunctionToolCallback.builder("getCurrentWeather", new WeatherService())
                        .description(
                                "查找某个城市的天气状况和温度")
                        .inputType(WeatherService.Request.class)
                        .build()))
                .build();

        ChatResponse response = this.chatModel.call(new Prompt(messages, promptOptions));

        logger.info("Response: {}", response);

        return response.getResult().getOutput().getText();
    }
}
