package com.example.demo.web;

import com.example.demo.model.Author;
import com.example.demo.service.AuthorService;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    @Mock
    private AuthorService authorService;
    @InjectMocks
    private AuthorController controller;

    @Test
    void addAuthor() {
        // prep
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));

        // when
        Mockito.when(authorService.addAuthor(author)).thenReturn(author);

        // act + assert
        assertThat(controller.addAuthor(author)).satisfies(createdAuthor -> {
            assertThat(createdAuthor.getId()).isEqualTo("ABC123");
            assertThat(createdAuthor.getName()).isEqualTo("steve");
            assertThat(createdAuthor.getCountry()).isEqualTo("france");
            assertThat(createdAuthor.getBirthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
        });

        // verify
        Mockito.verify(authorService).addAuthor(author);
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void getAllAuthors_pagination() {
        Author author = new Author("AXX2213", "FM", "SWE", LocalDate.of(1877, 2, 1));

        // when
        Mockito.when(authorService.getAll("", 1, 5)).thenReturn(List.of(author));

        // act + assert
        assertThat(controller.getAllAuthors("", 1, 5))
                .hasSize(1);
    }
    // pagination
    @ParameterizedTest
    @CsvSource({
            "-1, 0",
            "0, -1",
            "-1, -2"
    })
    void getAllAuthors_paginationWithNegativeNumber(int from, int to) {
        assertThatThrownBy(() -> controller.getAllAuthors("", from, to))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"parameters from and to must be greater than 0\"");
        verifyNoMoreInteractions(authorService);
    }
    // pagination
    @ParameterizedTest
    @CsvSource({
            "10, 0",
            "5, 4"
    })
    void getAllAuthors_paginationWithFromGreaterTo(int from, int to) {
        assertThatThrownBy(() -> controller.getAllAuthors("", from, to))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"parameter from must be greater than to\"");
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void getAllAuthors_emptyAuthorName() {
        // prep
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));

        // when
        Mockito.when(authorService.getAll("", 0, 5)).thenReturn(List.of(author));

        // act + assert
        assertThat(controller.getAllAuthors("", 0, 5))
                .isNotEmpty()
                .hasSize(1)
                .satisfies(createdAuthor -> {
                    assertThat(createdAuthor.getId()).isEqualTo("ABC123");
                    assertThat(createdAuthor.getName()).isEqualTo("steve");
                    assertThat(createdAuthor.getCountry()).isEqualTo("france");
                    assertThat(createdAuthor.getBirthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
                }, atIndex(0));

        // verify
        Mockito.verify(authorService).getAll("", 0, 5);
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void getAuthor() {
        // prep
        Author author = new Author("XD123", "steve", "france", LocalDate.of(1985, 4, 15));

        // when
        Mockito.when(authorService.getAuthor("XD123")).thenReturn(author);

        // act + assert
        assertThat(controller.getAuthor("XD123")).satisfies(createdAuthor -> {
            assertThat(createdAuthor.getId()).isEqualTo("XD123");
            assertThat(createdAuthor.getName()).isEqualTo("steve");
            assertThat(createdAuthor.getCountry()).isEqualTo("france");
            assertThat(createdAuthor.getBirthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
        });

        // verify
        Mockito.verify(authorService).getAuthor("XD123");
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void getAuthorNotFound() {
        // when
        Mockito.when(authorService.getAuthor("XD123")).thenReturn(null);

        // act + assert
        assertThat(controller.getAuthor("XD123")).isNull();

        // verify
        Mockito.verify(authorService).getAuthor("XD123");
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void removeAuthor() {
        // act
        controller.removeAuthor("FN123");

        Mockito.verify(authorService).deleteAuthor("FN123");
    }

    @Test
    void updateAuthor() {
        Author author = new Author("FN123", "lara", "USA", LocalDate.of(1985, 4, 15));

        Mockito.when(authorService.updateAuthor("FN123", author)).thenReturn(author);

        assertThat(controller.updateAuthor("FN123", author)).satisfies(updatedAuthor -> {
            assertThat(updatedAuthor.getId()).isEqualTo("FN123");
            assertThat(updatedAuthor.getName()).isEqualTo("lara");
            assertThat(updatedAuthor.getCountry()).isEqualTo("USA");
            assertThat(updatedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
        });
    }
}