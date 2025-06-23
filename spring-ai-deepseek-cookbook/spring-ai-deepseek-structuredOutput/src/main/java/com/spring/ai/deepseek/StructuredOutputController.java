package com.spring.ai.deepseek;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 结构化输出控制器
 *
 * 该控制器用于演示如何从 DeepSeek 模型中获取结构化输出（如 List、Map 和 Bean）。
 * 提供了三种接口：
 * - /output/list：获取字符串列表形式的输出
 * - /output/map：获取键值对形式的输出
 * - /output/bean：获取 Java Bean 形式的结构化输出
 *
 * @author 寻道AI小兵
 */
@RestController
@RequestMapping("/output")
public class StructuredOutputController {

    private final DeepSeekChatModel chatModel;

    @Autowired
    public StructuredOutputController(DeepSeekChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 获取字符串列表形式的输出
     * 示例请求：GET /output/list
     * @return 包含五个唐代诗人的字符串列表
     */
    @GetMapping("/list")
    public List<String> listOutputConverter() {
        DefaultConversionService conversionService = new DefaultConversionService();
        ListOutputConverter outputConverter = new ListOutputConverter(conversionService);

        String format = outputConverter.getFormat();
        String template = """
				列出五个 {subject}
				{format}
				""";
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(template)
                .variables(Map.of("subject", "唐代著名诗人", "format", format))
                .build();
        Prompt prompt = new Prompt(promptTemplate.createMessage());

        ChatResponse chatResponse = this.chatModel.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        List<String> result =outputConverter.convert(assistantMessage.getText());
        return result;

    }
    /**
     * 获取键值对形式的输出
     *
     * 示例请求：GET /output/map
     */
    @GetMapping("/map")
    public Map<String, Object> mapOutputConverter() {
        MapOutputConverter outputConverter = new MapOutputConverter();

        String format = outputConverter.getFormat();
        String template = """
				   提供一个JSON响应，不要使用任何代码块标记，例如 ```json```.
				给我一首 {subject}
				{format}
				""";
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(template)
                .variables(Map.of("subject", "李白的著作", "format",
                        format))
                .build();
        Prompt prompt = new Prompt(promptTemplate.createMessage());

        ChatResponse chatResponse = this.chatModel.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        Map<String, Object> result =outputConverter.convert(assistantMessage.getText());
        return result;
    }

    /**
     * 获取 Java Bean 形式的结构化输出
     * 示例请求：GET /output/bean
     * @return 包含演员及其电影作品的 Java Bean 对象
     */
    @GetMapping("/bean")
    public ActorsFilms beanOutputConverter() {

        BeanOutputConverter<ActorsFilms> outputConverter = new BeanOutputConverter<>(ActorsFilms.class);

        String format = outputConverter.getFormat();
        String template = """
				生成一个随机中国演员的电影作品集。
				提供一个JSON响应，不要使用任何代码块标记，例如 ```json```。
				{format}
				""";
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(template)
                .variables(Map.of("format", format))
                .build();
        Prompt prompt = new Prompt(promptTemplate.createMessage());

        ChatResponse chatResponse = this.chatModel.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        ActorsFilms actorsFilms = outputConverter.convert(assistantMessage.getText());

        return actorsFilms;
    }

}
