package com.spring.ai.openai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Function工具类，提供与图书馆管理相关的功能
 */
@Component
public class Tools {

    // 日志记录器实例，用于记录日志信息
    private static final Logger logger = LoggerFactory.getLogger(Tools.class);

    // 图书服务实例，用于执行与图书相关的操作
    private final BookService bookService = new BookService();

    /**
     * 欢迎特定用户进入图书馆
     *
     * @param user 需要欢迎的用户名字
     */
    @Tool(description = "欢迎特定用户进入图书馆")
    void welcomeUser(String user) {
        logger.info("欢迎 {} 进入图书馆", user);
    }

    /**
     * 获取图书馆中由指定作者撰写的书籍列表
     *
     * @param author 作者名字
     * @return 由指定作者撰写的书籍列表
     */
    @Tool(description = "获取图书馆中由指定作者撰写的书籍列表")
    List<BookService.Book> booksByAuthor(String author) {
        logger.info("获取作者 {} 的书籍列表", author);
        return bookService.getBooksByAuthor(new BookService.Author(author));
    }
}
