package com.example.demo.web;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.atIndex;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    @Mock
    private BookService bookService;
    @InjectMocks
    private BookController controller;

    @Test
    void addAuthor() {

        Book book = new Book(12L, 5, "Animals in the Ocean", LocalDate.of(1985, 4, 15));

        Mockito.when(bookService.addBook(book)).thenReturn(book);


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

        Mockito.when(bookService.getAll("", 1, 5)).thenReturn(List.of(book, book1));

        assertThat(controller.getBooks("", 1, 5))
            .hasSize(2);
    }

    // pagination
    @ParameterizedTest
    @CsvSource({
        "-1, 0",
        "0, -1",
        "-1, -2"
    })
    void getBooks_paginationWithNegativeNumber(int from, int to) {
        assertThatThrownBy(() -> controller.getBooks("", from, to))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessage("400 BAD_REQUEST \"parameters from and to must be greater than 0\"");
        verifyNoMoreInteractions(bookService);
    }

    // pagination
    @ParameterizedTest
    @CsvSource({
        "10, 0",
        "5, 4"
    })
    void getBooks_paginationWithFromGreaterTo(int from, int to) {
        assertThatThrownBy(() -> controller.getBooks("", from, to))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessage("400 BAD_REQUEST \"parameter from must be greater than to\"");
        verifyNoMoreInteractions(bookService);
    }

    @Test
    void getBooks_emptyBookName() {
        Book book = new Book(10L, 2, "RadioFM", LocalDate.of(1985, 4, 15));

        Mockito.when(bookService.getAll("", 0, 5)).thenReturn(List.of(book));

        assertThat(controller.getBooks("", 0, 5))
            .isNotEmpty()
            .hasSize(1)
            .satisfies(createdBook -> {
                assertThat(createdBook.id()).isEqualTo(10L);
                assertThat(createdBook.authorId()).isEqualTo(2);
                assertThat(createdBook.name()).isEqualTo("RadioFM");
                assertThat(createdBook.publishDate()).isEqualTo(LocalDate.of(1985, 4, 15));
            }, atIndex(0));

        Mockito.verify(bookService).getAll("", 0, 5);
        verifyNoMoreInteractions(bookService);
    }
}
