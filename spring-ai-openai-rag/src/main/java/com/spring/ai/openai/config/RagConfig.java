package com.spring.ai.openai.config;

import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * 配置类用于定义文档转换器的Bean
 */
@Configuration
public class RagConfig {

    /**
     * 创建并返回一个文档转换器实例
     * 该方法配置了一个TokenTextSplitter，用于将文档分割成 tokens
     * 这对于处理大型文档或需要按块处理文本的应用场景非常有用
     *
     * @return DocumentTransformer 实例，用于转换文档
     */
    @Bean
    public DocumentTransformer documentTransformer() {
        return new TokenTextSplitter();
    }

    @Bean
    public RestClient.Builder builder() {
        return RestClient.builder().requestFactory(new SimpleClientHttpRequestFactory());
    }

    @Bean
    public ChromaApi chromaApi(RestClient.Builder restClientBuilder) {
        String chromaUrl = "http://localhost:8000";
        ChromaApi chromaApi = new ChromaApi(chromaUrl, restClientBuilder);
        return chromaApi;
    }
    @Bean
    public VectorStore chromaVectorStore(EmbeddingModel embeddingModel, ChromaApi chromaApi) {
        return ChromaVectorStore.builder(chromaApi, embeddingModel)
                .collectionName("TestCollection")
                .initializeSchema(true)
                .build();
    }
}

