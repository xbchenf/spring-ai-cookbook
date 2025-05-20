package com.spring.ai.zhipu.controller;


import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 寻道AI小兵
 * @since 2025-4-15 15:19:51
 *
 * GLM-4v 模型图片理解示例
 */
@RestController
@RequestMapping("/glm")
public class ImageModelController {

    private final ChatModel chatModel;
    private final ImageModel imageModel;

    @Autowired
    public ImageModelController(ZhiPuAiChatModel chatModel,ZhiPuAiImageModel imageModel) {
        this.chatModel = chatModel;
        this.imageModel=imageModel;
    }

    @GetMapping("/multimodal/chat1")
    public String multimodal1(@RequestParam(value = "message", defaultValue = "请帮我描述一下这张图片") String message) {
        ClassPathResource imageResource = new ClassPathResource("/multimodal-test.png");

        UserMessage userMessage = new UserMessage(
                "Explain what do you see in this picture?", // content
                new Media(MimeTypeUtils.IMAGE_PNG, imageResource)); // media

        ChatResponse response = chatModel.call(new Prompt(userMessage));
        return response.getResult().getOutput().getText();
    }

    @GetMapping("/multimodal/chat2")
    public ImageResponse multimodal2(@RequestParam(value = "message", defaultValue = "一只小猫") String message) {
        ImageResponse response =imageModel.call(new ImagePrompt(message));
        return response;
    }

}