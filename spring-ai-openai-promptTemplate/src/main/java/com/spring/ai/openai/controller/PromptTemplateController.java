package com.spring.ai.openai.controller;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 控制器类，用于处理与生成诗歌和代码相关的请求。
 * 该类使用了OpenAI的聊天模型来生成诗歌和代码。
 *
 * @author 寻道AI小兵
 * @since 2025-4-5 20:59:16
 *
 * 主要功能：
 * - 提供三个端点来生成诗歌和代码，使用不同的模板和参数。
 */
@RestController
@RequestMapping("/prompt")
public class PromptTemplateController {

    // OpenAI聊天模型，用于生成文本
    private final OpenAiChatModel chatModel;

    // 从类路径加载的主题模板资源
    @Value("classpath:theme.st")
    private Resource templateResource;

    // 从类路径加载的代码模板资源
    @Value("classpath:code.st")
    private Resource codeTemplate;

    /**
     * 构造函数，初始化聊天模型
     *
     * @param chatModel 聊天模型
     */
    @Autowired
    public PromptTemplateController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 根据给定的主题生成五言律诗。
     * 使用字符串模板来创建提示词，并将其传递给聊天模型以生成诗歌。
     *
     * @param theme 用于生成诗歌的主题
     * @return 生成的诗歌作为AssistantMessage返回
     */
    @GetMapping("/generateFiveWordPoem1")
    public AssistantMessage generateFiveWordPoem1(@RequestParam String theme) {
        // 提示词模板
        final String template = "请以{theme}为主题，写一首五言律诗。";
        PromptTemplate promptTemplate = new PromptTemplate(template);
        // 动态地将theme填充进去
        Prompt prompt = promptTemplate.create(Map.of("theme", theme));

        ChatResponse chatResponse = chatModel.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        return assistantMessage;
    }

    /**
     * 使用文件模板生成五言律诗。
     * 从文件中读取模板，并使用给定的主题生成诗歌。
     *
     * @param theme 用于生成诗歌的主题
     * @return 生成的诗歌作为AssistantMessage返回
     */
    @GetMapping("/generateFiveWordPoem2")
    public AssistantMessage generateFiveWordPoem2(@RequestParam String theme) {
        // 使用文件模板
        PromptTemplate promptTemplate = new PromptTemplate(templateResource);
        // 动态地将theme填充进去
        Prompt prompt = promptTemplate.create(Map.of("theme", theme));
        ChatResponse chatResponse = chatModel.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        return assistantMessage;
    }

    /**
     * 根据描述、编程语言和方法名生成代码。
     * 使用代码模板来生成特定编程语言的代码片段。
     *
     * @param description 代码的功能描述
     * @param language 编程语言
     * @param methodName 方法名
     * @return 生成的代码片段作为字符串返回
     */
    @GetMapping("/code")
    public String generateCode(@RequestParam String description, @RequestParam String language, @RequestParam String methodName) {
        // 使用代码模板
        PromptTemplate promptTemplate = new PromptTemplate(codeTemplate);
        Prompt prompt = promptTemplate.create(
                Map.of("description", description, "language", language, "methodName", methodName)
        );
        ChatResponse chatResponse = chatModel.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        return assistantMessage.getText();
    }

}
