package com.spring.ai.ollama;

import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

/**
 * 嵌入向量生成控制器
 * 提供基于 Ollama 模型的文本嵌入向量生成服务
 * 支持中英文文本的向量化处理
 *
 * @author 寻道AI小兵
 * @since 2025-6-23
 */
@RestController
@RequestMapping("/ollama")
public class EmbeddingController {

    @Autowired
    private OllamaEmbeddingModel embeddingModel;
    /**
     * 文本嵌入向量生成接口（基础版本）
     *
     * @param message 待向量化的文本内容，默认值为 "Hello World"
     * @return 返回文本的嵌入向量结果
     *
     */
    @GetMapping("/embedding1")
    public Embedding embedding1(@RequestParam(value = "message", defaultValue = "Hello World") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return embeddingResponse.getResult();
    }

    /**
     * 文本嵌入向量生成接口（自定义模型配置）
     *
     * @param message 待向量化的文本内容，默认值为 "Hello World"
     * @return 返回文本的嵌入向量结果
     *
     */
    @GetMapping("/embedding2")
    public Embedding embedding2(@RequestParam(value = "message", defaultValue = "Hello World") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.call(new EmbeddingRequest(
                List.of(message), OllamaOptions.builder().model("bge-large:335m").truncate(false).build()));

        return embeddingResponse.getResult();
    }
}