package com.spring.ai.openai.controller;

import com.spring.ai.openai.model.Poem;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 控制器类，用于处理与生成诗歌和代码相关的请求。
 * 该类使用了OpenAI的聊天模型来生成诗歌和代码。
 *
 * @author 寻道AI小兵
 * @version 1.0
 * @since 2025-4-5 20:59:16
 *
 * 主要功能：
 * - 提供三个端点来生成诗歌，并将生成的结果以不同的格式返回。
 */
@RestController
@RequestMapping("/output")
public class StructuredOutputController {

    // OpenAI聊天模型，用于生成文本
    private final OpenAiChatModel chatModel;

    /**
     * 构造函数，初始化聊天模型
     *
     * @param chatModel 聊天模型
     */
    @Autowired
    public StructuredOutputController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 根据给定的作者生成一首诗。
     * 使用模板来请求聊天模型生成指定作者的最受欢迎的诗，并将其解析为Poem对象。
     *
     * @param author 诗的作者
     * @return 生成的诗作为Poem对象返回
     */
    @GetMapping("/generatePoemByAuthor1")
    public Poem generatePoemByAuthor1(@RequestParam String author) {
        final String template = """
                        请推荐一首诗人{author}写的最受欢迎的诗，要包含作者名称author，作者所属朝代year，诗的标题theme，诗的内容context
                        {format}
                        """;
        // 定义一个输出解析器，将生成的文本解析为Poem对象
        BeanOutputConverter<Poem> outputConverter = new BeanOutputConverter<>(Poem.class);
        PromptTemplate userPromptTemplate = new PromptTemplate(template);
        Prompt prompt = userPromptTemplate.create(Map.of("author", author, "format", outputConverter.getFormat()));
        // 调用聊天模型生成回复
        ChatResponse chatResponse = chatModel.call(prompt);
        // 解析输出
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        Poem poem = outputConverter.convert(assistantMessage.getText());
        return poem;
    }

    /**
     * 根据给定的作者生成一首诗。
     * 使用模板来请求聊天模型生成指定作者的最受欢迎的诗，并将其解析为Map对象。
     *
     * @param author 诗的作者
     * @return 生成的诗作为Map对象返回
     */
    @GetMapping("/generatePoemByAuthor2")
    public Map<String, Object> generatePoemByAuthor2(@RequestParam String author) {
        MapOutputConverter outputConverter = new MapOutputConverter();
        final String template = """
                        请推荐一首诗人{author}写的最受欢迎的诗，要包含作者名称author，作者所属朝代year，诗的标题theme，诗的内容context
                        {format}
                        """;
        PromptTemplate userPromptTemplate = new PromptTemplate(template);
        Prompt prompt = userPromptTemplate.create(Map.of("author", author, "format", outputConverter.getFormat()));
        // 调用聊天模型生成回复
        ChatResponse chatResponse = chatModel.call(prompt);
        // 解析输出
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        return outputConverter.convert(assistantMessage.getText());
    }

    /**
     * 根据给定的作者生成一首诗。
     * 使用模板来请求聊天模型生成指定作者的最受欢迎的诗，并将其解析为List对象。
     *
     * @param poem 包含作者信息的Poem对象
     * @return 生成的诗作为List对象返回
     */
    @PostMapping("/generatePoemByAuthor3")
    public List<String> generatePoemByAuthor3(@RequestBody Poem poem) {
        ListOutputConverter outputConverter = new ListOutputConverter(new DefaultConversionService());
        final String template = """
                        请推荐一首诗人{author}写的最受欢迎的诗，要包含作者名称author，作者所属朝代year，诗的标题theme，诗的内容context
                        {format}
                        """;
        PromptTemplate userPromptTemplate = new PromptTemplate(template);
        Prompt prompt = userPromptTemplate.create(Map.of(
                "author", poem.getAuthor(),
                "format", outputConverter.getFormat()));
        // 调用聊天模型生成回复
        ChatResponse chatResponse = chatModel.call(prompt);
        // 解析输出
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        return outputConverter.convert(assistantMessage.getText());
    }
}
