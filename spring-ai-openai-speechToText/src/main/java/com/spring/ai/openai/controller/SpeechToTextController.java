package com.spring.ai.openai.controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 语音转文本控制器，使用OpenAI的音频转录模型处理音频文件并转换为文本。
 *
 * @author 寻道AI小兵
 * @since 2025-4-5 20:59:16
 *
 * 主要功能：
 * - 提供两个端点来处理音频文件的转录，一个使用默认选项，另一个使用自定义选项。
 */
@RestController
@RequestMapping("/speech")
public class SpeechToTextController {

    // 注入OpenAI音频转录模型
    private final OpenAiAudioTranscriptionModel transcriptionModel;

    /**
     * 构造函数，初始化音频转录模型
     *
     * @param transcriptionModel 音频转录模型
     */
    @Autowired
    public SpeechToTextController(OpenAiAudioTranscriptionModel transcriptionModel) {
        this.transcriptionModel = transcriptionModel;
    }

    /**
     * 接收一个音频文件并返回其转录的文本。
     * 使用默认的转录选项。
     *
     * @param audioFile 音频文件资源，从类路径加载
     * @return 转录的文本内容
     */
    @GetMapping("/transcription")
    public String transcription(@Value("classpath:speech1.mp3") Resource audioFile) {
        return transcriptionModel.call(new AudioTranscriptionPrompt(audioFile)).getResult().getOutput();
    }

    /**
     * 接收一个音频文件并使用指定的转录选项返回其转录的文本。
     * 演示了如何使用不同的语言、提示、温度和响应格式进行转录。
     *
     * @param audioFile 音频文件资源，从类路径加载
     * @return 根据指定选项转录的文本内容
     */
    @GetMapping("/transcription2")
    public String transcription2(@Value("classpath:speech3.wav") Resource audioFile) {
        var transcriptionResponse = transcriptionModel.call(new AudioTranscriptionPrompt(audioFile, OpenAiAudioTranscriptionOptions.builder()
                .language("en") // 设置语言为英语
                .prompt("Ask not this, but ask that") // 用于引导转录模型在生成文本时参考此提示，以提高转录结果的相关性和准确性
                .temperature(0f) // 设置生成文本的随机性为0，即确定性最高
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.VTT) // 设置响应格式为VTT
                .build()));
        return transcriptionResponse.getResult().getOutput();
    }

}
