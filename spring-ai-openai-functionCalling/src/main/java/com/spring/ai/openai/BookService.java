package com.spring.ai.openai;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RegisterReflectionForBinding(classes = BookService.Book.class)
public class BookService {

    private static final Map<Integer,Book> books = new ConcurrentHashMap<>();

    static {
        books.put(1, new Book("围城", "钱钟书"));
        books.put(2, new Book("写在人生边上", "钱钟书"));
        books.put(3, new Book("人·兽·鬼", "钱钟书"));
        books.put(4, new Book("活着", "余华"));
        books.put(5, new Book("红楼梦", "曹雪芹"));
        books.put(6, new Book("西游记", "吴承恩"));
        books.put(7, new Book("水浒传", "施耐庵"));
    }

    public List<Book> getBooksByAuthor(Author author) {
        return books.values().stream()
                .filter(book -> author.name().equals(book.author()))
                .toList();
    }

    public List<Author> getAuthorsByBook(List<Book> booksToSearch) {
        return books.values().stream()
                .filter(book -> booksToSearch.stream()
                        .anyMatch(b -> b.title().equals(book.title())))
                .map(book -> new Author(book.author()))
                .toList();
    }

    public record Author(String name) {}
    public record Book(String title, String author) {}

    public record Authors(List<Author> authors) {}
    public record Books(List<Book> books) {}

}
