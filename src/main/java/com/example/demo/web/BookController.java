package com.example.demo.web;

import com.example.demo.model.Book;
import com.example.demo.model.BooksEnvelopDto;
import com.example.demo.model.entity.BookEntity;
import com.example.demo.model.enums.SortOrder;
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
import java.util.Optional;

@RestController
@RequestMapping(value = {"/api/books"})
public class BookController implements IBookController {
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
    public BooksEnvelopDto getBooks(
        @RequestParam(value = "pageNumber") int pageNumber,
        @RequestParam(value = "pageSize") int pageSize,
        @RequestParam(value = "sortField", defaultValue = "NAME") BooksEnvelopDto.SortField sortField,
        @RequestParam(value = "sortOrder", defaultValue = "ASC") SortOrder sortOrder,
        @RequestParam(value = "maybeBookName", required = false) Optional<String> maybeBookName
    ) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parameters pageNumber and pageSize must be greater than 0");
        }
        if (pageNumber > pageSize) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parameter pageNumber must be greater than pageSize");
        }
        return bookService.getAll(pageNumber, pageSize, sortField, sortOrder, maybeBookName);
    }


    @GetMapping("/{bookId}")
    public Book getBook(@PathVariable Long bookId) {
        return bookService.getBook(bookId);
    }

    @GetMapping("/byAuthorId/{authorId}")
    public List<BookEntity> getBooksForAuthor() {
        return bookService.getBooksForAuthor();
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
