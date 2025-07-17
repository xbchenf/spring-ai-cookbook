package com.spring.ai.ollama;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 寻道AI小兵
 * @since 2025-6-23
 */
@RestController
@RequestMapping("/ollama")
public class StructuredOutputController {

    private static final Logger log = LoggerFactory.getLogger(StructuredOutputController.class);
    private static final String OLLAMA_MODEL = "llama3.1:8b";

    @Autowired
    private OllamaChatModel chatModel;

    @GetMapping("/list")
    public List<String> listOutputConverter(@RequestParam String message) {
        DefaultConversionService conversionService = new DefaultConversionService();
        ListOutputConverter outputConverter = new ListOutputConverter(conversionService);

        String format = outputConverter.getFormat();
        String template = """
				推荐五个 {subject}
				{format}
				""";
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(template)
                .variables(Map.of("subject", message, "format", format))
                .build();
        //Prompt prompt = new Prompt(promptTemplate.createMessage());
        //ChatResponse response = this.chatModel.call(prompt);
        ChatResponse response = chatModel.call(
                new Prompt(promptTemplate.createMessage(),
                        OllamaOptions.builder()
                                .model(OLLAMA_MODEL)
                                .temperature(0.4)
                                .build()
                ));
        log.info("mapOutputConverter - 模型响应内容: " + response.getResult().getOutput().getText());
        List<String> list = outputConverter.convert(response.getResult().getOutput().getText());
        return list;
    }


    @GetMapping("/map")
    public Map<String, Object> mapOutputConverter(@RequestParam String message) {
        MapOutputConverter outputConverter = new MapOutputConverter();

        String format = outputConverter.getFormat();
        String template = """
				请推荐一首诗人{author}写的最受欢迎的诗，要包含作者名称author，作者所属朝代year，诗的标题theme，诗的内容context：
				{format}
				""";
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(template)
                .variables(Map.of("author", message, "format", format))
                .build();
        //Prompt prompt = new Prompt(promptTemplate.createMessage());
        //ChatResponse response = this.chatModel.call(prompt);
        ChatResponse response = chatModel.call(
                new Prompt(promptTemplate.createMessage(),
                        OllamaOptions.builder()
                                .model(OLLAMA_MODEL)
                                .temperature(0.4)
                                .build()
                ));
        log.info("mapOutputConverter - 模型响应内容: " + response.getResult().getOutput().getText());
        Map<String, Object> result = outputConverter.convert(response.getResult().getOutput().getText());
        return result;
    }

    @GetMapping("/bean")
    public ActorsFilms beanOutputConverter(@RequestParam String message) {

        BeanOutputConverter<ActorsFilms> outputConverter = new BeanOutputConverter<>(ActorsFilms.class);

        String format = outputConverter.getFormat();
        String template = """
				请查找{actor}参演的最出名的3部作品：
				{format}
				""";
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(template)
                .variables(Map.of("actor",message,"format", format))
                .build();
        //Prompt prompt = new Prompt(promptTemplate.createMessage());
        //ChatResponse response = this.chatModel.call(prompt);
        ChatResponse response = chatModel.call(
                new Prompt(promptTemplate.createMessage(),
                        OllamaOptions.builder()
                                .model(OLLAMA_MODEL)
                                .temperature(0.4)
                                .build()
                ));
        log.info("mapOutputConverter - 模型响应内容: " + response.getResult().getOutput().getText());
        ActorsFilms actorsFilms = outputConverter.convert(response.getResult().getOutput().getText());
        return actorsFilms;
    }

}
