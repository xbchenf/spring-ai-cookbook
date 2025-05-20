package com.spring.ai.openai;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 使用低级ChatModel API的聊天示例控制器。
 * 该控制器提供了两种不同的方式来调用工具函数，以获取指定作者撰写的书籍列表。
 *
 * @author 寻道AI小兵
 * @since 2025-4-6 17:07:29
 */
@RestController
@RequestMapping("/model")
class ChatModelController {

    // BookService实例，用于业务逻辑
    private final BookService bookService;
    // ChatModel实例，用于与聊天模型交互
    private final ChatModel chatModel;

    // 构造函数，初始化BookService和ChatModel
    ChatModelController(BookService bookService, ChatModel chatModel) {
        this.bookService = bookService;
        this.chatModel = chatModel;
    }

    /**
     * 方式一：直接指定工具函数
     * 该方法通过直接定义一个工具函数来获取指定作者撰写的书籍列表。
     *
     * @param authorName 作者名称
     * @return 包含书籍信息的字符串
     */
    @GetMapping("/chat/function1")
    String function1(String authorName) {
        // 用户提示模板
        PromptTemplate userPromptTemplate = new PromptTemplate("""
                图书馆中有哪些作者 {author} 撰写的书籍？
                """);
        // 模板参数
        Map<String, Object> model = Map.of("author", authorName);
        // 创建Prompt对象，包含工具回调
        Prompt prompt = userPromptTemplate.create(model, ToolCallingChatOptions.builder()
                .toolCallbacks(
                        FunctionToolCallback.builder("booksByAuthor", bookService::getBooksByAuthor)
                                .description("获取图书馆中指定作者撰写的书籍列表")
                                .inputType(BookService.Author.class)
                                .build()
                )
                .build());

        // 调用ChatModel并获取响应
        ChatResponse chatResponse = chatModel.call(prompt);
        return chatResponse.getResult().getOutput().getText();
    }

    /**
     * 方式二：通过Functions配置类，指定工具函数
     * 该方法通过Functions配置类中的工具函数来获取指定作者撰写的书籍列表。
     *
     * @param authorName 作者名称
     * @return 包含书籍信息的字符串
     */
    @GetMapping("/chat/function2")
    String function2(String authorName) {
        // 用户提示模板
        PromptTemplate userPromptTemplate = new PromptTemplate("""
                图书馆中有哪些作者 {author} 撰写的书籍？
                """);
        // 模板参数
        Map<String, Object> model = Map.of("author", authorName);
        // 创建Prompt对象，包含工具名称
        Prompt prompt = userPromptTemplate.create(model, ToolCallingChatOptions.builder()
                .toolNames(Functions.BOOKS_BY_AUTHOR)
                .build());

        // 调用ChatModel并获取响应
        ChatResponse chatResponse = chatModel.call(prompt);
        return chatResponse.getResult().getOutput().getText();
    }

}
