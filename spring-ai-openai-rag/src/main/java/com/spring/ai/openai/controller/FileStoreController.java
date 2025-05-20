package com.spring.ai.openai.controller;

import com.spring.ai.openai.service.FileStoreService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储控制器类
 * 提供文件上传的接口
 */
@RequestMapping("/file")
@RestController
public class FileStoreController {
    // 文件存储服务
    private final FileStoreService fileStoreService;

    /**
     * 构造函数注入文件存储服务
     *
     * @param fileStoreService 文件存储服务实例
     */
    public FileStoreController(FileStoreService fileStoreService) {
        this.fileStoreService = fileStoreService;
    }

    /**
     * 文件上传接口
     * 接收前端传来的文件并调用服务层进行保存
     *
     * @param file 要上传的文件
     * @return 返回文件保存的结果，具体类型依赖于服务层的实现
     */
    @PostMapping("/upload")
    public Object uploadFile(MultipartFile file){
        return fileStoreService.saveFile(file);
    }
}
