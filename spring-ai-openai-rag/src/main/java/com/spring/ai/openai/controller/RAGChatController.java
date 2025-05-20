package com.spring.ai.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rag")
public class RAGChatController {

    @Value("classpath:rag_test.st")
    private Resource promptResource;

    private final ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    public RAGChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
    @GetMapping("/simpleChat")
    public String simpleChat(String prompt) {
        return chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/ragChat")
    public String ragChat(String prompt){
        // 从向量数据库中搜索相似文档
        List<Document> documents = vectorStore.similaritySearch(prompt);
        // 获取documents里的content
        List<String> context = documents.stream().map(Document::getText).toList();

        // 创建系统提示词
        SystemPromptTemplate promptTemplate = new SystemPromptTemplate(promptResource);
        // 填充数据
        Prompt p = promptTemplate.create(Map.of("context", context, "question", prompt));

        ChatResponse response = chatClient.prompt(p).call().chatResponse();
        return response.getResult().getOutput().getText();
    }

}