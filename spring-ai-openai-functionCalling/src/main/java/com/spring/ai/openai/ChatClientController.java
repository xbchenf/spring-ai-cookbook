package com.spring.ai.openai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 使用高级ChatClient API的聊天示例控制器。
 * 该控制器提供了三种不同的方式来调用工具函数，以获取指定作者撰写的书籍列表。
 *
 * @author 寻道AI小兵
 * @since 2025-4-6 17:07:29
 */
@RestController
@RequestMapping("/client")
class ChatClientController {

    // ChatClient实例，用于与聊天模型交互
    private final ChatClient chatClient;
    // Tools实例，包含多个工具函数
    private final Tools tools;

    // 构造函数，初始化ChatClient和Tools
    ChatClientController(ChatClient.Builder chatClientBuilder, Tools tools) {
        this.chatClient = chatClientBuilder.build();
        this.tools = tools;
    }

    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(ChatClientController.class);

    /**
     * 方式一：直接指定工具函数
     * 该方法通过直接定义一个工具函数来获取指定作者撰写的书籍列表。
     *
     * @param authorName 作者名称
     * @return 包含书籍信息的字符串
     */
    @GetMapping("/function1")
    String function1(String authorName) {
        // 定义工具函数，获取指定作者的书籍列表
        Function<BookService.Author, List<BookService.Book>> function = author -> {
            logger.info("CALLBACK - 获取作者 {} 的书籍: {}", author.name(), new BookService().getBooksByAuthor(author));
            return new BookService().getBooksByAuthor(author);
        };
        // 用户提示模板
        var userPromptTemplate = "图书馆中有哪些作者 {author} 撰写的书籍？";
        // 使用ChatClient发送请求并调用工具函数
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("author", authorName)
                )
                .tools(FunctionToolCallback.builder("availableBooksByAuthor", function)
                        .description("获取图书馆中指定作者撰写的书籍列表")
                        .inputType(BookService.Author.class)
                        .build())
                .call()
                .content();
    }

    /**
     * 方式二：通过Functions配置类，指定工具函数
     * 该方法通过Functions配置类中的工具函数来获取指定作者撰写的书籍列表。
     *
     * @param authorName 作者名称
     * @return 包含书籍信息的字符串
     */
    @GetMapping("/function2")
    String function2(String authorName) {
        // 用户提示模板
        var userPromptTemplate = "图书馆中有哪些作者 {author} 撰写的书籍？";
        // 使用ChatClient发送请求并调用Functions配置类中的工具函数
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("author", authorName)
                )
                .tools(Functions.BOOKS_BY_AUTHOR)
                .call()
                .content();
    }

    /**
     * 方式三：通过Tools类，指定工具函数
     * 该方法通过Tools类中的工具函数来获取指定作者撰写的书籍列表。
     *
     * @param authorName 作者名称
     * @return 包含书籍信息的字符串
     */
    @GetMapping("function3")
    String function3(String authorName) {
        // 用户提示模板
        var userPromptTemplate = "图书馆中有哪些作者 {author} 撰写的书籍？";
        // 使用ChatClient发送请求并调用Tools类中的工具函数
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("author", authorName)
                )
                .tools(tools)
                .call()
                .content();
    }

}
