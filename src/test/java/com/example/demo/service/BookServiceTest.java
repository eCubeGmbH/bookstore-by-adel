package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.BooksEnvelopDto;
import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.model.entity.BookEntity;
import com.example.demo.model.enums.SortOrder;
import com.example.demo.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {


    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;


    private final AuthorEntity authorEntity = new AuthorEntity("test", "blah", LocalDate.now(), new ArrayList<>());

    private final BookEntity bookEntity1 = new BookEntity(authorEntity, "Firstbook", LocalDate.of(1997, 1, 2));
    private final BookEntity BookEntity2 = new BookEntity(authorEntity, "Secondbook", LocalDate.of(1997, 1, 2));
    private final BookEntity BookEntity3 = new BookEntity(authorEntity, "Thirdbook", LocalDate.of(1997, 1, 2));


    @ParameterizedTest
    @ValueSource(strings = {"Firstbook", "firstbook", "FIRStbook", "  firstbook ", "\nfirstbook\t"})
    void paginationTestWithFiltering2(String bookName) {
        PageRequest pageRequest = PageRequest.of(0, 2).withSort(Sort.by("name").ascending());
        Page<BookEntity> page = new PageImpl<>(List.of(bookEntity1), pageRequest, 1L);

        when(bookRepository.findByNameIgnoreCase(eq(bookName.trim()), any(Pageable.class))).thenReturn(page);

        BooksEnvelopDto result = bookService.getAll(0, 2, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.of(bookName.trim()));

        // act
        Assertions.assertThat(result.bookList()).extracting(Book::name).contains("Firstbook");
        // act
        Assertions.assertThat(result.booksCount()).isEqualTo(1);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "   ", "\t", " \n "})
    void test_empty(String bookName) {
        PageRequest pageRequest1 = PageRequest.of(0, 3).withSort(Sort.by("name").ascending());
        Page<BookEntity> page1 = new PageImpl<>(List.of(bookEntity1, BookEntity2, BookEntity3), pageRequest1, 3L);

        when(bookRepository.findAll(pageRequest1)).thenReturn(page1);

        // act + assert
        BooksEnvelopDto result = bookService.getAll(0, 3, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.ofNullable(bookName));

        assertThat(result.bookList()).extracting(Book::name).contains("Firstbook", "Secondbook", "Thirdbook");
        assertThat(result.booksCount()).isEqualTo(3L);
    }

    @Test
    void allBooks_nameNotFound() {
        PageRequest pageRequest = PageRequest.of(0, 3).withSort(Sort.by("name").ascending());
        Page<BookEntity> page = new PageImpl<>(List.of(bookEntity1, BookEntity2, BookEntity3), pageRequest, 3L);

        when(bookRepository.findByNameIgnoreCase("weirdName", pageRequest)).thenReturn(page);
        // act + assert
        BooksEnvelopDto result = bookService.getAll(0, 3, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.of("weirdName"));

        assertThat(result.bookList()).isNotEmpty();
        //
        verify(bookRepository).findByNameIgnoreCase("weirdName", PageRequest.of(0, 3).withSort(Sort.by("name").ascending()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Fourthbook", "fourthbook", "FOUrthbook", "  Fourthbook ", "\nFourthbook\t"})
    void allBooks_bookFound(String name) {
        PageRequest pageRequest = PageRequest.of(0, 3).withSort(Sort.by("name").ascending());
        Page<BookEntity> page = new PageImpl<>(List.of(bookEntity1, BookEntity2, BookEntity3), pageRequest, 3L);

        when(bookRepository.findByNameIgnoreCase(name.trim(), pageRequest)).thenReturn(page);

        // act + assert
        BooksEnvelopDto result = bookService.getAll(0, 3, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.of(name));
        assertThat(result.bookList()).isNotEmpty();

    }

    @Test
    void allBooks_bookFoundTwice() {

        PageRequest pageRequest = PageRequest.of(0, 3).withSort(Sort.by("name").ascending());
        Page<BookEntity> page = new PageImpl<>(List.of(bookEntity1, BookEntity2, BookEntity3), pageRequest, 3L);

        when(bookRepository.findByNameIgnoreCase("Thirdbook", pageRequest)).thenReturn(page);

        // act + assert
        BooksEnvelopDto result = bookService.getAll(0, 3, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.of("Thirdbook"));
        assertThat(result.bookList()).isNotEmpty();
    }

}
