package com.spring.ai.openai.controller;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingOptionsBuilder;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 控制器类，处理与嵌入（Embedding）相关的HTTP请求。
 * 使用嵌入模型生成文本的嵌入向量，并支持不同的配置选项。
 *
 * @author 寻道AI小兵
 * @since 2025-4-3 15:53:23
 *
 * 主要功能：
 * - 提供多个端点来处理不同配置的嵌入请求。
 */
@RestController
@RequestMapping("/embedding")
public class EmbeddingController {

    // 注入嵌入模型依赖
    private final EmbeddingModel embeddingModel;

    /**
     * 构造函数注入嵌入模型
     *
     * @param embeddingModel 嵌入模型实例
     */
    public EmbeddingController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    /**
     * 处理 /embed 请求，返回查询文本的嵌入向量大小。
     *
     * @param query 输入的查询文本
     * @return 返回嵌入向量的大小
     */
    @GetMapping("/embed")
    public String embed(@RequestParam String query) {
        var embeddings = embeddingModel.embed(query);
        return "Size of the embedding vector: " + embeddings.length;
    }

    /**
     * 处理 /embed/generic-options 请求，使用通用选项进行文本嵌入。
     *
     * @param query 输入的查询文本
     * @return 返回嵌入向量的大小
     */
    @GetMapping("/embed/generic-options")
    public String embedGenericOptions(@RequestParam String query) {
        var embeddings = embeddingModel.call(new EmbeddingRequest(List.of(query), EmbeddingOptionsBuilder.builder()
                        .withModel(OpenAiApi.EmbeddingModel.TEXT_EMBEDDING_3_SMALL.getValue())
                        .build()))
                .getResult().getOutput();
        return "Size of the embedding vector: " + embeddings.length;
    }

    /**
     * 处理 /embed/provider-options 请求，使用特定供应商选项进行文本嵌入。
     *
     * @param query 输入的查询文本
     * @return 返回嵌入向量的大小
     */
    @GetMapping("/embed/provider-options")
    public String embedProviderOptions(@RequestParam String query) {
        var embeddings = embeddingModel.call(new EmbeddingRequest(List.of(query), OpenAiEmbeddingOptions.builder()
                        .encodingFormat("float")
                        .build()))
                .getResult().getOutput();
        return "Size of the embedding vector: " + embeddings.length;
    }

}
