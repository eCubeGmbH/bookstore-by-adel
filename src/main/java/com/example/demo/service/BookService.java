package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.model.entity.BookEntity;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.web.InvalidDataException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    final static String errorMessage = "The book you requested doesn't exist. Please review your parameters!";

    public Book addBook(Book book) {
        Optional<AuthorEntity> authorEntity = authorRepository.findById(book.authorId());
        return authorEntity.map(author -> {
            BookEntity bookEntity = new BookEntity(author, book.name(), book.publishDate());
            BookEntity savedBookEntity = bookRepository.save(bookEntity);
            return toBook(savedBookEntity);
        }).orElseThrow(() -> new InvalidDataException(""));
    }

    public List<Book> getAll(String bookName, int from, int to) {
        // Sorting
        Sort sortOrder = Sort.by("name").ascending()
            .and(Sort.by("id").ascending());
        // Pagination + Sorting
        int pageSize = to - from;
        int pageNumber = from / pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize).withSort(sortOrder);

        List<BookEntity> bookEntities = (bookName == null || bookName.isBlank())
            ? bookRepository.findAll(pageRequest).getContent()
            : bookRepository.findByNameIgnoreCase(bookName.trim(), pageRequest);

        return bookEntities.stream()
            .map(this::toBook)
            .toList();
    }

    public Book getBook(Long bookId) {
        return toBook(findBookAndValidate(bookId));
    }

//    public List<BookEntity> getBooksForAuthor(long authorId) {
//        return bookRepository.findByAuthorId(authorId);
//    }

    public void deleteBook(Long bookId) {
        BookEntity foundBook = findBookAndValidate(bookId);
        bookRepository.delete(foundBook);
    }

    public Book updateBook(Long bookId, Book bookFromUser) {
        BookEntity foundBook = findBookAndValidate(bookId);
        foundBook.setName(bookFromUser.name());
        foundBook.setPublishDate(bookFromUser.publishDate());
        BookEntity savedBookEntity = bookRepository.save(foundBook);
        return toBook(savedBookEntity);
    }

    private BookEntity findBookAndValidate(Long bookId) {
        Optional<BookEntity> maybeBookEntity = bookRepository.findById(bookId);
        if (maybeBookEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        } else {
            return maybeBookEntity.get();
        }
    }

    private Book toBook(BookEntity bookEntity) {
        return new Book(bookEntity.getId(), bookEntity.getAuthorReference().getId(), bookEntity.getName(), bookEntity.getPublishDate());
    }

    public List<BookEntity> getBooksForAuthor() {
        return List.of();
    }
}
