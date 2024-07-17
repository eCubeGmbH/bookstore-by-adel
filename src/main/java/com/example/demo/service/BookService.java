package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.BooksEnvelopDto;
import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.model.entity.BookEntity;
import com.example.demo.model.enums.SortOrder;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.web.InvalidDataException;
import org.springframework.data.domain.Page;
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
    final static String authorErrorMessage = "The author you requested doesn't exist. Please review your parameters!";


    public Book addBook(Book book) {
        Optional<AuthorEntity> authorEntity = authorRepository.findById(book.authorId());
        return authorEntity.map(author -> {
            BookEntity bookEntity = new BookEntity(author, book.name(), book.publishDate());
            BookEntity savedBookEntity = bookRepository.save(bookEntity);
            return toBook(savedBookEntity);
        }).orElseThrow(() -> new InvalidDataException(authorErrorMessage));
    }

    public BooksEnvelopDto getAll(int pageNumber, int pageSize, BooksEnvelopDto.SortField sortField, SortOrder sortOrder, Optional<String> maybeBookName) {
        // Sorting
        Sort sort = Sort.by(Sort.Direction.valueOf(sortOrder.name()), sortField.getFieldName());

        // Pagination + Sorting
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<BookEntity> booksCount = maybeBookName
            .map(name -> bookRepository.findByNameIgnoreCase(name.trim(), pageRequest))
            .orElseGet(() -> bookRepository.findAll(pageRequest));

        List<Book> books = booksCount.getContent().stream()
            .map(this::toBook)
            .toList();

        return new BooksEnvelopDto(
            pageNumber,
            pageSize,
            booksCount.getTotalElements(),
            sortField,
            sortOrder,
            maybeBookName.orElse(null),
            books
        );
    }

    public Book getBook(Long bookId) {
        return toBook(findBookAndValidate(bookId));
    }

    public void deleteBook(Long bookId) {
        BookEntity foundBook = findBookAndValidate(bookId);
        bookRepository.delete(foundBook);
    }

    private Book toBook(BookEntity bookEntity) {
        return new Book(bookEntity.getId(), bookEntity.getAuthorReference().getId(), bookEntity.getName(), bookEntity.getPublishDate());
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


    public List<BookEntity> getBooksForAuthor() {
        return List.of();
    }
}
