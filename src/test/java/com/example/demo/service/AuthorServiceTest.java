package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.model.AuthorsEnvelopDto;
import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.model.enums.SortField;
import com.example.demo.model.enums.SortOrder;
import com.example.demo.repository.AuthorRepository;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private final AuthorEntity authorEntity1 = new AuthorEntity(UUID.randomUUID().toString(), "John", "USA", LocalDate.of(1997, 1, 2), new ArrayList<>());
    private final AuthorEntity authorEntity2 = new AuthorEntity(UUID.randomUUID().toString(), "Müller", "USA", LocalDate.of(1997, 1, 2), new ArrayList<>());
    private final AuthorEntity authorEntity3 = new AuthorEntity(UUID.randomUUID().toString(), "müller", "USA", LocalDate.of(1997, 1, 2), new ArrayList<>());
    private final AuthorEntity authorEntity4 = new AuthorEntity(UUID.randomUUID().toString(), "Meier", "USA", LocalDate.of(1997, 1, 2), new ArrayList<>());
    private final AuthorEntity authorEntity5 = new AuthorEntity(UUID.randomUUID().toString(), "Rein", "DÄN", LocalDate.of(1920, 1, 2), new ArrayList<>());
    private final AuthorEntity authorEntity6 = new AuthorEntity(UUID.randomUUID().toString(), "Weg", "SWE", LocalDate.of(1911, 11, 9), new ArrayList<>());
    private final AuthorEntity authorEntity7 = new AuthorEntity(UUID.randomUUID().toString(), "Frank", "SYR", LocalDate.of(1991, 2, 2), new ArrayList<>());
    private final AuthorEntity authorEntity8 = new AuthorEntity(UUID.randomUUID().toString(), "FNG", "CHI", LocalDate.of(1755, 2, 22), new ArrayList<>());

    @Test
    void paginationTest1() {
        /*
         * Page 0
         */
        PageRequest pageRequest1 = PageRequest.of(0, 3).withSort(Sort.by("name").ascending());
        Page<AuthorEntity> page1 = new PageImpl<>(List.of(authorEntity1, authorEntity2, authorEntity3), pageRequest1, 5L);

        when(authorRepository.findAll(pageRequest1)).thenReturn(page1);

        // act
        AuthorsEnvelopDto result1 = authorService.getAll(0, 3, SortField.NAME, SortOrder.ASC, Optional.empty());

        // assert
        assertThat(result1.authors()).extracting(Author::name).contains("John", "Müller", "müller");
        assertThat(result1.authorsCount()).isEqualTo(5L);

        /*
         * Page 1
         */
        PageRequest pageRequest2 = PageRequest.of(1, 3).withSort(Sort.by("name").ascending());
        Page<AuthorEntity> page2 = new PageImpl<>(List.of(authorEntity4, authorEntity5, authorEntity6), pageRequest2, 8L);

        when(authorRepository.findAll(pageRequest2)).thenReturn(page2);

        // act
        AuthorsEnvelopDto result2 = authorService.getAll(1, 3, SortField.NAME, SortOrder.ASC, Optional.empty());

        // assert
        assertThat(result2.authors()).extracting(Author::name).contains("Meier", "Rein", "Weg");
        assertThat(result2.authorsCount()).isEqualTo(8L);

        /*
         * Page 2
         */
        PageRequest pageRequest3 = PageRequest.of(2, 2).withSort(Sort.by("name").ascending());
        Page<AuthorEntity> page3 = new PageImpl<>(List.of(authorEntity7, authorEntity8), pageRequest3, 8L);

        when(authorRepository.findAll(pageRequest3)).thenReturn(page3);

        // act
        AuthorsEnvelopDto result3 = authorService.getAll(2, 2, SortField.NAME, SortOrder.ASC, Optional.empty());

        // assert
        assertThat(result3.authors()).extracting(Author::name).contains("Frank", "FNG");
        assertThat(result3.authorsCount()).isEqualTo(8L);
    }

    @Test
    void paginationTestWithFiltering() {
        PageRequest pageRequest = PageRequest.of(0, 2).withSort(Sort.by("name").ascending());
        Page<AuthorEntity> page = new PageImpl<>(List.of(authorEntity1), pageRequest, 1L);

        when(authorRepository.findByName(eq("John"), any(Pageable.class))).thenReturn(page);

        // act
        AuthorsEnvelopDto result = authorService.getAll(0, 2, SortField.NAME, SortOrder.ASC, Optional.of("John"));

        // assert
        assertThat(result.authors()).extracting(Author::name).contains("John");
        assertThat(result.authorsCount()).isEqualTo(1L);
    }

    @ParameterizedTest
    @ValueSource(strings = {"John", "john", "JOHn", "  John ", "\njohn\t"})
    void paginationTestWithFiltering2(String authorName) {
        PageRequest pageRequest = PageRequest.of(0, 11).withSort(Sort.by("id").ascending());
        Page<AuthorEntity> page = new PageImpl<>(List.of(authorEntity1), pageRequest, 1L);

        when(authorRepository.findByName(authorName.trim(), pageRequest)).thenReturn(page);

        // act
        AuthorsEnvelopDto result = authorService.getAll(0, 11, SortField.ID, SortOrder.ASC, Optional.of(authorName));

        // assert
        assertThat(result.authors()).extracting(Author::name).contains("John");
        assertThat(result.authorsCount()).isEqualTo(1L);

        verify(authorRepository).findByName(authorName.trim(), pageRequest);
        verifyNoMoreInteractions(authorRepository);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "   ", "\t", " \n "})
    void test_empty(String authorName) {
        PageRequest pageRequest1 = PageRequest.of(0, 3).withSort(Sort.by("name").ascending());
        Page<AuthorEntity> page1 = new PageImpl<>(List.of(authorEntity1, authorEntity2, authorEntity3), pageRequest1, 3L);

        when(authorRepository.findAll(pageRequest1)).thenReturn(page1);

        // act
        AuthorsEnvelopDto result = authorService.getAll(0, 3, SortField.NAME, SortOrder.ASC, Optional.ofNullable(authorName));

        // assert
        assertThat(result.authors()).extracting(Author::name).contains("John", "Müller", "müller");
        assertThat(result.authorsCount()).isEqualTo(3L);
    }

    @Test
    void allAuthors_nameNotFound() {
        PageRequest pageRequest = PageRequest.of(0, 11).withSort(Sort.by("id").ascending());
        Page<AuthorEntity> page = new PageImpl<>(List.of(), pageRequest, 0L);

        when(authorRepository.findByName("steve", pageRequest)).thenReturn(page);

        // act
        AuthorsEnvelopDto result = authorService.getAll(0, 11, SortField.ID, SortOrder.ASC, Optional.of("steve"));

        // assert
        assertThat(result.authors()).isEmpty();
        assertThat(result.authorsCount()).isEqualTo(0L);

        verify(authorRepository).findByName("steve", pageRequest);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Meier", "meier", "mEier", "  Meier ", "\nMeier\t"})
    void allAuthors_authorFound(String authorName) {
        PageRequest pageRequest = PageRequest.of(0, 11).withSort(Sort.by("name").ascending());
        Page<AuthorEntity> page = new PageImpl<>(List.of(authorEntity4), pageRequest, 1L);

        when(authorRepository.findByName(authorName.trim(), pageRequest)).thenReturn(page);

        // act
        AuthorsEnvelopDto result = authorService.getAll(0, 11, SortField.NAME, SortOrder.ASC, Optional.of(authorName));

        // assert
        assertThat(result.authors()).hasSize(1).extracting(Author::name).containsExactly("Meier");
        assertThat(result.authorsCount()).isEqualTo(1L);

        when(authorRepository.findByName(authorName.trim(), pageRequest)).thenReturn(page);

        result = authorService.getAll(0, 11, SortField.NAME, SortOrder.ASC, Optional.of(authorName));

        assertThat(result.authors()).isNotEmpty();
    }

    @Test
    void allAuthors_authorFoundTwice() {
        PageRequest pageRequest = PageRequest.of(0, 11, Sort.by("name").ascending());
        Page<AuthorEntity> page = new PageImpl<>(List.of(authorEntity2, authorEntity3), pageRequest, 2L);

        when(authorRepository.findByName("Müller", pageRequest)).thenReturn(page);

        // act
        AuthorsEnvelopDto result = authorService.getAll(0, 11, SortField.NAME, SortOrder.ASC, Optional.of("Müller"));

        // assert
        assertThat(result.authors()).hasSize(2).extracting(Author::name).containsExactlyInAnyOrder("Müller", "müller");
        assertThat(result.authorsCount()).isEqualTo(2L);
    }
}
