package com.example.demo.web;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping(value = {"/api/books"})
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping()
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @GetMapping()
    public List<Book> getAllBooks() {
        return bookService.getAll();
    }

    @GetMapping()
    public Book getBook(@PathVariable long bookId) {
        return bookService.getBook(bookId);
    }

    @GetMapping()
    public List<Book> getBooksForAuthor(@PathVariable String authorId) {
        return bookService.getBooksForAuthor(authorId);
    }

    @DeleteMapping()
    public void deleteBook(@PathVariable long bookId) {
        bookService.deleteBook(bookId);
    }

    @PutMapping()
    public Book updateBook(@PathVariable long bookId, @RequestBody Book bookFromUser) {
        return bookService.updateBook(bookId, bookFromUser);
    }

}
