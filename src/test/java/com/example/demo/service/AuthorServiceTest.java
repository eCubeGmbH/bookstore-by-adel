package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private AuthorEntity AuthorEntity1 = new AuthorEntity(UUID.randomUUID().toString(), "John", "USA", LocalDate.of(1997, 1, 2));
    private AuthorEntity AuthorEntity2 = new AuthorEntity(UUID.randomUUID().toString(), "Müller", "USA", LocalDate.of(1997, 1, 2));
    private AuthorEntity AuthorEntity3 = new AuthorEntity(UUID.randomUUID().toString(), "müller", "USA", LocalDate.of(1997, 1, 2));
    private AuthorEntity AuthorEntity4 = new AuthorEntity(UUID.randomUUID().toString(), "Meier", "USA", LocalDate.of(1997, 1, 2));
    private AuthorEntity AuthorEntity5 = new AuthorEntity(UUID.randomUUID().toString(), "Rein", "DÄN", LocalDate.of(1920, 1, 2));
    private AuthorEntity AuthorEntity6 = new AuthorEntity(UUID.randomUUID().toString(), "Weg", "SWE", LocalDate.of(1911, 11, 9));
    private AuthorEntity AuthorEntity7 = new AuthorEntity(UUID.randomUUID().toString(), "Frank", "SYR", LocalDate.of(1991, 2, 2));
    private AuthorEntity AuthorEntity8 = new AuthorEntity(UUID.randomUUID().toString(), "FNG", "CHI", LocalDate.of(1755, 2, 22));

    private List<AuthorEntity> allAuthors = List.of(
            AuthorEntity1,
            AuthorEntity2,
            AuthorEntity3,
            AuthorEntity4,
            AuthorEntity5,
            AuthorEntity6,
            AuthorEntity7,
            AuthorEntity8
    );

    @Test
    void paginationTest1() {
        when(authorRepository.findAll()).thenReturn(allAuthors);

        // act
        assertThat(authorService.getAll("", 0, 3))
                .extracting(Author::name).contains("FNG", "Frank", "John");
        // act
        assertThat(authorService.getAll("", 3, 6))
                .extracting(Author::name).contains("Meier", "Müller", "müller");
        // act
        assertThat(authorService.getAll("", 6, 9))
                .extracting(Author::name).contains("Rein", "Weg");
        // act
        assertThat(authorService.getAll("", 9, 12))
                .isEmpty();
    }

    @Test
    void paginationTestWithFiltering() {

        Sort sortOrder = Sort.by("name").ascending()
                .and(Sort.by("id").ascending());

        when(authorRepository.findByName(eq("John"), any(Pageable.class)))
                .thenReturn(List.of(AuthorEntity1))
                .thenReturn(List.of());

        // act
        assertThat(authorService.getAll("John", 0, 2))
                .extracting(Author::name).contains("John");
        // act
        assertThat(authorService.getAll("John", 9, 11))
                .isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"John", "john", "JOHn", "  John ", "\njohn\t"})
    void paginationTestWithFiltering2(String authorName) {
        when(authorRepository.findAll()).thenReturn(allAuthors);

        // act
        assertThat(authorService.getAll(authorName, 0, 2))
                .extracting(Author::name).contains("John");
        // act
        assertThat(authorService.getAll(authorName, 9, 11))
                .isEmpty();

    }


    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "   ", "\t", " \n "})
    void test_empty(String authorName) {
        when(authorRepository.findAll()).thenReturn(allAuthors);

        // act + assert
        assertThat(authorService.getAll(authorName, 0, 11))
                .isNotEmpty()
                .hasSize(8);
    }

    @Test
    void allAuthors_nameNotFound() {
        when(authorRepository.findAll()).thenReturn(allAuthors);

        // act + assert
        assertThat(authorService.getAll("steve", 0, 11))
                .isEmpty();
        //
        verify(authorRepository).findAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Meier", "meier", "mEier", "  Meier ", "\nMeier\t"})
    void allAuthors_authorFound(String authorName) {
        when(authorRepository.findAll()).thenReturn(allAuthors);

        // act + assert
        assertThat(authorService.getAll(authorName, 0, 11))
                .hasSize(1)
                .extracting(Author::name).containsExactly("Meier");
    }

    @Test
    void allAuthors_authorFoundTwice() {
        when(authorRepository.findAll()).thenReturn(allAuthors);

        // act + assert
        assertThat(authorService.getAll("MülLer", 0, 11))
                .hasSize(2)
                .extracting(Author::name).containsExactlyInAnyOrder("Müller", "müller");
    }
}
