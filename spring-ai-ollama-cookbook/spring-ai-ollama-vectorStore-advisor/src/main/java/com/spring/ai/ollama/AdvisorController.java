package com.spring.ai.ollama;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于 Chroma 向量数据库的知识库问答控制器
 * 通过Spring AI中的Advisor实现RAG（Retrieval-Augmented Generation）架构的问答功能
 * 提供两个接口实现：
 * - questionAnswerAdvisor1 使用默认搜索参数的问答接口
 * - questionAnswerAdvisor2 使用自定义搜索参数的问答接口
 *
 * @author 寻道AI小兵
 * @since 2025-6-25
 */
@RestController
@RequestMapping("/advisor")
public class AdvisorController {

    private static final Logger logger = LoggerFactory.getLogger(AdvisorController.class);

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private VectorStore vectorStore;

    /**
     * 使用默认搜索参数的问答接口
     * 通过QuestionAnswerAdvisor实现基于向量相似度的文档检索和答案生成
     *
     * @param message 用户输入的查询语句
     * @return 生成的回答结果文本
     */
    @GetMapping("/questionAnswerAdvisor")
    public String questionAnswerAdvisor1(@RequestParam(value = "message", defaultValue = "message") String message) {
        ChatResponse response = ChatClient.builder(chatModel)
                .build().prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .user(message)
                .call()
                .chatResponse();
        return response.getResult().getOutput().getText();
    }

}