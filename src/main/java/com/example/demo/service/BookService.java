package com.example.demo.service;

import com.example.demo.model.Book;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    private final Map<Long, Book> bookMap = new HashMap<>();

    public Book addBook(Book book) {
        bookMap.put(book.id(), book);
        return book;
    }

    public List<Book> getAll() {
        return new ArrayList<>(bookMap.values());
    }

    public Book getBook(long bookId) {
        return bookMap.get(bookId);
    }

    public List<Book> getBooksForAuthor(String authorId) {
        List<Book> booksForAuthor = new ArrayList<>();
        for (Book book : bookMap.values()) {
            if (book.authorId().equals(authorId)) {
                booksForAuthor.add(book);
            }
        }
        return booksForAuthor;
    }

    public void deleteBook(long bookId) {
        bookMap.remove(bookId);
    }

    public Book updateBook(long bookId, Book book) {
        bookMap.replace(bookId, book);
        return book;
    }


}
