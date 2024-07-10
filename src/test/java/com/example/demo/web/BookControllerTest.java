package com.example.demo.web;

import com.example.demo.model.Book;
import com.example.demo.model.BooksEnvelopDto;
import com.example.demo.model.enums.SortOrder;
import com.example.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    @Mock
    private BookService bookService;
    @InjectMocks
    private BookController controller;

    @Test
    void addAuthor() {

        Book book = new Book(12L, 5, "Animals in the Ocean", LocalDate.of(1985, 4, 15));

        when(bookService.addBook(book)).thenReturn(book);


        assertThat(controller.addBook(book)).satisfies(createdBook -> {
            assertThat(createdBook.id()).isEqualTo(12L);
            assertThat(createdBook.authorId()).isEqualTo(5);
            assertThat(createdBook.name()).isEqualTo("Animals in the Ocean");
            assertThat(createdBook.publishDate()).isEqualTo(LocalDate.of(1985, 4, 15));
        });

        Mockito.verify(bookService).addBook(book);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    void getBooks_pagination() {
        Book book = new Book(15L, 7, "BCE", LocalDate.of(1877, 2, 1));
        Book book1 = new Book(16L, 8, "NFTs", LocalDate.of(1877, 2, 1));
        List<Book> bookList = List.of(book, book1);
        BooksEnvelopDto envelopDto = new BooksEnvelopDto(1, 2, 2, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, null, bookList);

        when(bookService.getAll(0, 2, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.empty())).thenReturn(envelopDto);

        assertThat(controller.getBooks(0, 2, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.empty())).satisfies(envelopeDto -> {
            assertThat(envelopeDto.pageNumber()).isEqualTo(1);
            assertThat(envelopeDto.pageSize()).isEqualTo(2);
            assertThat(envelopeDto.booksCount()).isEqualTo(2);
            assertThat(envelopeDto.sortField()).isEqualTo(BooksEnvelopDto.SortField.NAME);
            assertThat(envelopeDto.sortOrder()).isEqualTo(SortOrder.ASC);
            assertThat(envelopeDto.bookList()).containsExactly(book, book1);
        });
    }

    // pagination
    @ParameterizedTest
    @CsvSource({
        "-1, 0",
        "0, -1",
        "-1, -2"
    })
    void getBooks_paginationWithNegativeNumber(int pageNumber, int pageSize) {
        assertThatThrownBy(() -> controller.getBooks(pageNumber, pageSize, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.empty()))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("parameters pageNumber and pageSize must be greater than 0")
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
        verifyNoMoreInteractions(bookService);
    }

    // pagination
    @ParameterizedTest
    @CsvSource({
        "5, 4"
    })
    void getBooks_paginationWithPageNumberGreaterPageSize(int pageNumber, int pageSize) {
        assertThatThrownBy(() -> controller.getBooks(pageNumber, pageSize, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.empty()))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("parameter pageNumber must be greater than pageSize")
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    void getBooks_emptyBookName() {
        Book book = new Book(10L, 2, "RadioFM", LocalDate.of(1985, 4, 15));

        List<Book> bookList = List.of(book);
        BooksEnvelopDto envelopDto = new BooksEnvelopDto(1, 1, 1, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, "RadioFM", bookList);

        when(bookService.getAll(0, 1, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.empty())).thenReturn(envelopDto);

        assertThat(controller.getBooks(0, 1, BooksEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.empty())).satisfies(envelopeDto -> {
            assertThat(envelopeDto.pageNumber()).isEqualTo(1);
            assertThat(envelopeDto.pageSize()).isEqualTo(1);
            assertThat(envelopeDto.booksCount()).isEqualTo(1);
            assertThat(envelopeDto.sortField()).isEqualTo(BooksEnvelopDto.SortField.NAME);
            assertThat(envelopeDto.sortOrder()).isEqualTo(SortOrder.ASC);
            assertThat(envelopeDto.bookList()).containsExactly(book);
        });
        verifyNoMoreInteractions(bookService);
    }

}
