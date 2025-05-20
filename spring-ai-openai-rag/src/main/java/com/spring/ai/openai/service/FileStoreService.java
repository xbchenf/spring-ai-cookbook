package com.spring.ai.openai.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件存储服务类，负责处理文件上传并将其内容存储到向量库中
 */
@Service
public class FileStoreService {
    // 向量库，用于存储文档的向量表示
    private final VectorStore vectorStore;
    // 文档转换器，用于转换文档
    private final DocumentTransformer documentTransformer;

    /**
     * 构造函数，注入向量库和文档转换器
     *
     * @param vectorStore 向量库实例
     * @param documentTransformer 文档转换器实例
     */
    public FileStoreService(VectorStore vectorStore, DocumentTransformer documentTransformer) {
        this.vectorStore = vectorStore;
        this.documentTransformer = documentTransformer;
    }

    /**
     * 保存文件到向量库
     *
     * @param file 要保存的文件
     * @return 如果上传成功，返回"上传成功"，否则返回异常原因
     */
    public Object saveFile(MultipartFile file){
        try {
            // 获取文件资源
            Resource resource = file.getResource();
            // 使用TikaDocumentReader读取文件内容并转换为文档列表
            TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
            List<Document> fileDocuments = tikaDocumentReader.get();
            // 使用文档转换器对文件文档列表进行转换
            List<Document> documents = documentTransformer.apply(fileDocuments);
            // 将转换后的文档列表存储到向量库
            vectorStore.accept(documents);
            return "上传成功";
        }catch (Exception e){
            // 返回异常原因
            return e.getCause();
        }
    }
}
