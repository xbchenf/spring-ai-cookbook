package com.spring.ai.ollama;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * RAG（Retrieval-Augmented Generation）服务控制器
 * 提供文档上传、存储和检索功能
 */
@RestController
@RequestMapping("/rag")
public class RagController {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ChatModel chatModel;

    //@Value("${classpath:谜语问答游戏.docx}")
    private String filePath="D:\\github\\spring-ai-cookbook\\spring-ai-ollama-cookbook\\spring-ai-ollama-vectorStore-rag\\src\\main\\resources\\谜语问答游戏.docx";

    //@Value("${classpath:rag-prompt-template.txt}")
    private String promptTemplatePath="D:\\github\\spring-ai-cookbook\\spring-ai-ollama-cookbook\\spring-ai-ollama-vectorStore-rag\\src\\main\\resources\\rag-prompt-template.txt";

    /**
     * 上传文档并将其存储到Chroma向量数据库
     * @param filePath 文档文件路径
     */
    @GetMapping("/upload")
    public String uploadDocument(/*@RequestParam String filePath*/) {
        try {
            // 加载文档
            Resource resource = new FileSystemResource(filePath);
            TikaDocumentReader documentReader = new TikaDocumentReader(resource);
            List<Document> documents = documentReader.get();
            // 文档切片处理
           // DocumentTransformer documentTransformer = new RecursiveCharacterTextSplitter(chunkSize, chunkOverlap);
            TokenTextSplitter splitter = new TokenTextSplitter(1000, 400, 10, 5000, true);

            List<Document> transformedDocuments = splitter.apply(documents);

            // 生成向量并存储到Chroma
            vectorStore.add(transformedDocuments);

            return "Document uploaded and stored successfully: " + filePath;
        } catch (Exception e) {
            return "Error uploading document: " + e.getMessage();
        }
    }

    /**
     * 执行相似性搜索
     * @param query 查询内容
     */
    @GetMapping("/search")
    public List<Document> searchDocuments(@RequestParam String query) {
        // 创建搜索请求
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(5)
                .build();

        // 执行相似性搜索
        return this.vectorStore.similaritySearch(searchRequest);
    }

    /**
     * 执行RAG查询，结合检索结果生成回答
     * @param query 查询内容
     */
    @GetMapping("/ask")
    public String askQuestion(@RequestParam String query) {
        // 获取相关文档
        List<Document> results = searchDocuments(query);

        // 构建上下文信息
        StringBuilder contextBuilder = new StringBuilder();
        for (Document doc : results) {
            contextBuilder.append(doc.getText()).append("\n");
        }
        String context = contextBuilder.toString();
        // 使用系统提示模板构建提示词
        SystemPromptTemplate promptTemplate = new SystemPromptTemplate(promptTemplatePath);
        Message systemMessage = promptTemplate.createMessage(Map.of("context", context, "question", query));

        UserMessage userMessage = new UserMessage(query);

        var portableOptions = ChatOptions.builder().temperature(0.7).build();
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), portableOptions);

        // 调用大模型生成回答
        ChatResponse response = chatModel.call(prompt);
        Generation generation = response.getResult();

        // 返回生成的回答
        return generation.getOutput().getText();
    }

}