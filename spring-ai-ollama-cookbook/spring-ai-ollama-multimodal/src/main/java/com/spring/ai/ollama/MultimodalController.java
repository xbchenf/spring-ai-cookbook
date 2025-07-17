package com.spring.ai.ollama;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 多模态聊天控制器
 * 提供图像识别与描述生成的交互接口
 * 支持通过图片进行AI对话
 *
 * @author 寻道AI小兵
 * @since 2025-6-23
 */
@RestController
@RequestMapping("/ollama")
public class MultimodalController {

    @Autowired
    private OllamaChatModel chatModel;

    /**
     * 图像识别接口
     * 加载本地测试图片并请求模型生成对图片内容的描述
     *
     * 注意事项：
     * - 使用 PNG 格式图片
     * - 返回结果通过控制台输出
     */
    @GetMapping("/image")
    public String multiModalityTest() {
       // Resource imageData = new ClassPathResource("/animal.png");
        Resource imageData = new FileSystemResource("D:\\github\\spring-ai-cookbook\\spring-ai-ollama-cookbook\\spring-ai-ollama-multimodal\\src\\main\\resources\\animal.png");
        UserMessage userMessage = UserMessage.builder()
                .text("图片中有什么内容？")
                .media(List.of(new Media(MimeTypeUtils.IMAGE_PNG, imageData)))
                .build();
        ChatResponse response = this.chatModel.call(new Prompt(List.of(userMessage), ChatOptions.builder().model("qwen2.5vl:7b").build()));
        System.out.println(response.getResult().getOutput().getText());
        return response.getResult().getOutput().getText();
    }
}
