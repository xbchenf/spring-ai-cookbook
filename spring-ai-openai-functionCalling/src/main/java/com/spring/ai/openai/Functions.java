package com.spring.ai.openai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Functions配置类，用于定义与图书馆操作相关的功能Bean。
 * 该类声明了几个Bean，这些Bean执行与图书馆中的书籍和作者相关的特定操作或计算。
 */
@Configuration(proxyBeanMethods = false)
public class Functions {

    // Bean名称常量
    public static final String AUTHORS_BY_BOOKS = "authorsByBooks";
    public static final String BOOKS_BY_AUTHOR = "booksByAuthor";
    public static final String WELCOME = "welcome";
    public static final String WELCOME_USER = "welcomeUser";

    // 类的日志记录器
    private static final Logger logger = LoggerFactory.getLogger(Functions.class);

    // 业务逻辑服务实例
    private final BookService bookService = new BookService();

    /**
     * Bean定义，用于欢迎用户到图书馆。
     * 该Bean表示一个操作，记录欢迎用户的日志消息。
     *
     * @return 一个Consumer，不接受任何输入，在调用时记录欢迎消息。
     */
    @Bean(WELCOME)
    @Description("欢迎用户到图书馆")
    Consumer<Void> welcome() {
        return (input) -> logger.info("欢迎用户到图书馆");
    }

    /**
     * Bean定义，用于欢迎特定用户到图书馆。
     * 该Bean表示一个操作，记录特定用户的个性化欢迎日志消息。
     *
     * @return 一个Consumer，接受一个User对象作为输入，在调用时记录个性化的欢迎消息。
     */
    @Bean(WELCOME_USER)
    @Description("欢迎特定用户到图书馆")
    Consumer<User> welcomeUser() {
        return user -> logger.info("欢迎 {} 到图书馆", user.name());
    }

    /**
     * Bean定义，用于获取特定作者撰写的书籍列表。
     * 该Bean表示一个函数，给定一个作者时，返回该作者撰写的书籍列表。
     *
     * @return 一个Function，接受一个Author对象作为输入，返回一个Book对象列表。
     */
    @Bean(BOOKS_BY_AUTHOR)
    @Description("获取图书馆中指定作者撰写的书籍列表")
    Function<BookService.Author, List<BookService.Book>> booksByAuthor() {
        return author -> {
            logger.info("获取作者 {} 的书籍: {}", author.name(), bookService.getBooksByAuthor(author).stream().map(BookService.Book::title).toList());
            return bookService.getBooksByAuthor(author);
        };
    }

    /**
     * Bean定义，用于获取撰写指定书籍的作者列表。
     * 该Bean表示一个函数，给定一组书籍时，返回撰写这些书籍的作者列表。
     *
     * @return 一个Function，接受一个Books对象作为输入，返回一个Author对象列表。
     */
    @Bean(AUTHORS_BY_BOOKS)
    @Description("获取图书馆中撰写指定书籍的作者列表")
    Function<BookService.Books, List<BookService.Author>> authorsByBooks() {
        return books -> {
            logger.info("获取书籍 {} 的作者: {}", books.books().stream().map(BookService.Book::title).toList(), bookService.getAuthorsByBook(books.books()).stream().map(BookService.Author::name).toList());
            return bookService.getAuthorsByBook(books.books());
        };
    }

    // 用户记录类，包含用户名
    public record User(String name) {}

}
