package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private static final List<Author> allAuthors = List.of(
            new Author("1", "John", "USA", LocalDate.of(1997, 1, 2)),
            new Author("2", "Müller", "USA", LocalDate.of(1997, 1, 2)),
            new Author("3", "müller", "USA", LocalDate.of(1997, 1, 2)),
            new Author("4", "Meier", "USA", LocalDate.of(1997, 1, 2)),
            new Author("5", "Rein", "DÄN", LocalDate.of(1920, 1, 2)),
            new Author("6", "Weg", "SWE", LocalDate.of(1911, 11, 9)),
            new Author("7", "Frank", "SYR", LocalDate.of(1991, 2, 2)),
            new Author("8", "FNG", "CHI", LocalDate.of(1755, 2, 22))
    );

    @Test
    void paginationTest1() {
        when(authorRepository.getAll()).thenReturn(allAuthors);

        // act
        assertThat(authorService.getAll("", 0, 3))
                .extracting(Author::getName).contains("FNG", "Frank", "John");
        // act
        assertThat(authorService.getAll("", 3, 6))
                .extracting(Author::getName).contains("Meier", "Müller", "müller");
        // act
        assertThat(authorService.getAll("", 6, 9))
                .extracting(Author::getName).contains("Rein", "Weg");
        // act
        assertThat(authorService.getAll("", 9, 12))
                .isEmpty();
    }

    @Test
    void paginationTestWithFiltering() {
        when(authorRepository.getAll()).thenReturn(allAuthors);

        // act
        assertThat(authorService.getAll("John", 0, 2))
                .extracting(Author::getName).contains("John");
        // act
        assertThat(authorService.getAll("John", 9, 11))
                .isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"John", "john", "JOHn", "  John ", "\njohn\t"})
    void paginationTestWithFiltering2(String authorName) {
        when(authorRepository.getAll()).thenReturn(allAuthors);

        // act
        assertThat(authorService.getAll(authorName, 0, 2))
                .extracting(Author::getName).contains("John");
        // act
        assertThat(authorService.getAll(authorName, 9, 11))
                .isEmpty();

    }


    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "   ", "\t", " \n "})
    void test_empty(String authorName) {
        when(authorRepository.getAll()).thenReturn(allAuthors);

        // act + assert
        assertThat(authorService.getAll(authorName, 0, 11))
                .isNotEmpty()
                .hasSize(8);
    }

    @Test
    void allAuthors_nameNotFound() {
        when(authorRepository.getAll()).thenReturn(allAuthors);

        // act + assert
        assertThat(authorService.getAll("steve", 0, 11))
                .isEmpty();
        //
        verify(authorRepository).getAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Meier", "meier", "mEier", "  Meier ", "\nMeier\t"})
    void allAuthors_authorFound(String authorName) {
        when(authorRepository.getAll()).thenReturn(allAuthors);

        // act + assert
        assertThat(authorService.getAll(authorName, 0, 11))
                .hasSize(1)
                .extracting(Author::getName).containsExactly("Meier");
    }

    @Test
    void allAuthors_authorFoundTwice() {
        when(authorRepository.getAll()).thenReturn(allAuthors);

        // act + assert
        assertThat(authorService.getAll("MülLer", 0, 11))
                .hasSize(2)
                .extracting(Author::getName).containsExactlyInAnyOrder("Müller", "müller");
    }
}
