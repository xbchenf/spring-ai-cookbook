package com.spring.ai.openai.controller;

import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * TextToSpeechController 提供了将文本转换为语音的功能
 * 通过调用 OpenAI 的 API，将指定的文本消息转换为语音输出
 *
 * @author 寻道AI小兵
 * @since 2025-4-6 16:47:46
 *
 *
 * 主要功能：
 * - 提供两个端点 `/speech1` 和 `/speech2`，用于将文本转换为语音。
 * - 提供一个端点 `/saveSpeech`，用于将文本转换为语音并保存为文件。
 */
@RestController
public class TextToSpeechController {

    // 注入 SpeechModel 用于处理文本到语音的转换
    private final SpeechModel speechModel;

    /**
     * 构造函数注入 SpeechModel
     *
     * @param speechModel 用于文本到语音转换的模型
     */
    TextToSpeechController(SpeechModel speechModel) {
        this.speechModel = speechModel;
    }

    /**
     * 将提供的文本消息转换为语音
     * 此方法使用默认的语音合成选项将文本转换为语音
     *
     * @param message 要转换为语音的文本消息
     * @return 转换后的语音数据，以字节数组形式返回
     */
    @GetMapping("/speech1")
    byte[] speech1(String message) {
        return speechModel.call(new SpeechPrompt(message)).getResult().getOutput();
    }

    /**
     * 将提供的文本消息转换为语音，并允许自定义语音合成选项
     * 此方法演示了如何使用特定的模型、声音、响应格式和速度来转换文本为语音
     *
     * @param message 要转换为语音的文本消息
     * @return 使用指定选项转换后的语音数据，以字节数组形式返回
     */
    @GetMapping("/speech2")
    byte[] speech2(String message) {
        // 创建并配置自定义的 SpeechPrompt，包括模型、声音、响应格式和速度
        var speechResponse = speechModel.call(new SpeechPrompt(message, OpenAiAudioSpeechOptions.builder()
                .model("tts-1")
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0f)
                .build()));
        return speechResponse.getResult().getOutput();
    }

    /**
     * 将提供的文本消息转换为语音并保存为文件
     *
     * @param message 要转换为语音的文本消息
     */
    @GetMapping("/saveSpeech")
    void saveSpeech(String message) {
        byte[] audioData = speech2(message);
        try (FileOutputStream fos = new FileOutputStream("D:\\github\\spring-ai-cookbook\\spring-ai-openai-textToSpeech\\test.mp3")) {
            fos.write(audioData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
