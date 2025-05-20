package com.spring.ai.openai.controller;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器类，用于处理与图像生成相关的HTTP请求。
 * 使用图像生成模型根据文本提示生成图像。
 *
 * @author 寻道AI小兵
 * @version 1.0
 * @since 2025-4-6 17:31:23
 *
 * 主要功能：
 * - 提供两个端点来处理不同配置的图像生成请求。
 */
@RestController
@RequestMapping("/img")
public class ImageController {

    // 注入图像生成模型依赖
    private final ImageModel imageModel;

    /**
     * 构造函数注入图像生成模型
     *
     * @param imageModel 图像生成模型实例
     */
    public ImageController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    /**
     * 处理 /image1 请求，根据文本提示生成图像。
     * 使用默认的图像生成选项生成图像。
     *
     * @param message 文本提示
     * @return 生成的图像的URL
     */
    @GetMapping("/image1")
    public String image1(@RequestParam String message) {
        var imageResponse = imageModel.call(new ImagePrompt(message, ImageOptionsBuilder.builder()
                .height(256)
                .width(256)
                .build()));
        return imageResponse.getResult().getOutput().getUrl();
    }

    /**
     * 处理 /image2 请求，根据文本提示生成图像。
     * 使用特定的图像生成选项生成图像。
     *
     * @param message 文本提示
     * @return 生成的图像的URL
     */
    @GetMapping("/image2")
    public String image2(@RequestParam String message) {
        var imageResponse = imageModel.call(new ImagePrompt(message, OpenAiImageOptions.builder()
                .quality("standard")
                .N(1)
                .height(1024)
                .width(1024)
                .model(OpenAiImageApi.ImageModel.DALL_E_3.getValue())
                .responseFormat("url")
                .build()));
        return imageResponse.getResult().getOutput().getUrl();
    }

}
