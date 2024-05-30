package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.model.entity.BookEntity;
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
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
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
    private final BookEntity BookEntity4 = new BookEntity(authorEntity, "Fourthbook", LocalDate.of(1997, 1, 2));
    private final BookEntity BookEntity5 = new BookEntity(authorEntity, "thirdbook", LocalDate.of(1920, 1, 2));


    @ParameterizedTest
    @ValueSource(strings = {"Firstbook", "firstbook", "FIRStbook", "  firstbook ", "\nfirstbook\t"})
    void paginationTestWithFiltering2(String bookName) {
        when(bookRepository.findByNameIgnoreCase("Firstbook", PageRequest.of(0, 11).withSort(Sort.by("name", "id").ascending())))
            .thenReturn(List.of(bookEntity1));

        // act
        Assertions.assertThat(bookService.getAll("Firstbook", 0, 11))
            .extracting(Book::name).contains("Firstbook");
        // act
        Assertions.assertThat(bookService.getAll(bookName, 9, 11))
            .isEmpty();

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "   ", "\t", " \n "})
    void test_empty(String name) {
        PageRequest pageRequest1 = PageRequest.of(0, 3).withSort(Sort.by("name", "id").ascending());
        Page<BookEntity> page1 = new PageImpl<>(List.of(bookEntity1, BookEntity2, BookEntity3), pageRequest1, 3L);

        when(bookRepository.findAll(pageRequest1)).thenReturn(page1);

        // act + assert
        List<Book> all = bookService.getAll(name, 0, 3);
        assertThat(all)
            .extracting(Book::name).contains("Firstbook", "Secondbook", "Thirdbook");
    }

    @Test
    void allBooks_nameNotFound() {
        when(bookRepository.findByNameIgnoreCase("weirdName", PageRequest.of(0, 11).withSort(Sort.by("name", "id").ascending())))
            .thenReturn(List.of());

        // act + assert
        assertThat(bookService.getAll("weirdName", 0, 11))
            .isEmpty();
        //
        verify(bookRepository).findByNameIgnoreCase("weirdName", PageRequest.of(0, 11).withSort(Sort.by("name", "id").ascending()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Fourthbook", "fourthbook", "FOUrthbook", "  Fourthbook ", "\nFourthbook\t"})
    void allBooks_bookFound(String name) {
        when(bookRepository.findByNameIgnoreCase(name.trim(), PageRequest.of(0, 11).withSort(Sort.by("name", "id").ascending())))
            .thenReturn(List.of(BookEntity4));

        // act + assert
        assertThat(bookService.getAll(name, 0, 11))
            .hasSize(1)
            .extracting(Book::name).containsExactly("Fourthbook");
        assertThat(bookService.getAll(name, 6, 9))
            .isEmpty();
    }

    @Test
    void allBooks_bookFoundTwice() {

        when(bookRepository.findByNameIgnoreCase("Thirdbook", PageRequest.of(0, 11, Sort.by("name", "id").ascending())))
            .thenReturn(List.of(BookEntity3, BookEntity5));

        // act + assert
        assertThat(bookService.getAll("Thirdbook", 0, 11))
            .hasSize(2)
            .extracting(Book::name).containsExactlyInAnyOrder("Thirdbook", "thirdbook");
    }
}
