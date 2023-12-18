package com.example.demo.web;

import com.example.demo.model.Book;
import com.example.demo.model.entity.BookEntity;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping(value = {"/api/books"})
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping()
    public Book addBook(@Valid @RequestBody Book book) {
        return bookService.addBook(book);
    }

    @GetMapping()
    public List<Book> getBooks(
        @RequestParam(value = "bookName", required = false, defaultValue = "") String bookName,
        @RequestParam(value = "from") int from,
        @RequestParam(value = "to") int to
    ) {
        if (from < 0 || to < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parameters from and to must be greater than 0");
        }
        if (from > to) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parameter from must be greater than to");
        }
        if (Math.abs(from - to) > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "result can  contains maximum 1000 elements");
        }
        return bookService.getAll(bookName, from, to);
    }


    @GetMapping("/{bookId}")
    public Book getBook(@PathVariable Long bookId) {
        return bookService.getBook(bookId);
    }

    @GetMapping("/byAuthorId/{authorId}")
    public List<BookEntity> getBooksForAuthor(@PathVariable String authorId) {
        return bookService.getBooksForAuthor(authorId);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable long bookId) {
        bookService.deleteBook(bookId);
    }

    @PutMapping("/{bookId}")
    public Book updateBook(@PathVariable long bookId, @RequestBody Book bookFromUser) {
        return bookService.updateBook(bookId, bookFromUser);
    }

}
