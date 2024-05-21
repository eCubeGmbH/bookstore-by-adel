package com.example.demo.service;

import com.example.demo.model.Author;
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
    private final AuthorEntity AuthorEntity2 = new AuthorEntity(UUID.randomUUID().toString(), "Müller", "USA", LocalDate.of(1997, 1, 2), new ArrayList<>());
    private final AuthorEntity AuthorEntity3 = new AuthorEntity(UUID.randomUUID().toString(), "müller", "USA", LocalDate.of(1997, 1, 2), new ArrayList<>());
    private final AuthorEntity AuthorEntity4 = new AuthorEntity(UUID.randomUUID().toString(), "Meier", "USA", LocalDate.of(1997, 1, 2), new ArrayList<>());
    private final AuthorEntity AuthorEntity5 = new AuthorEntity(UUID.randomUUID().toString(), "Rein", "DÄN", LocalDate.of(1920, 1, 2), new ArrayList<>());
    private final AuthorEntity AuthorEntity6 = new AuthorEntity(UUID.randomUUID().toString(), "Weg", "SWE", LocalDate.of(1911, 11, 9), new ArrayList<>());
    private final AuthorEntity AuthorEntity7 = new AuthorEntity(UUID.randomUUID().toString(), "Frank", "SYR", LocalDate.of(1991, 2, 2), new ArrayList<>());
    private final AuthorEntity AuthorEntity8 = new AuthorEntity(UUID.randomUUID().toString(), "FNG", "CHI", LocalDate.of(1755, 2, 22), new ArrayList<>());
    List<AuthorEntity> authors = List.of(authorEntity1, AuthorEntity2, AuthorEntity3, AuthorEntity4, AuthorEntity5);

    @Test
    void paginationTest1() {

        /*
         * Page 0
         */
        PageRequest pageRequest1 = PageRequest.of(0, 3).withSort(Sort.by("name").ascending());
        PageImpl<AuthorEntity> page1 = new PageImpl<>(
            List.of(
                authorEntity1,
                AuthorEntity2,
                AuthorEntity3
            ),
            pageRequest1,
            3L
        );
        when(authorRepository.findAll(pageRequest1))
            .thenReturn(page1);

        // act
        assertThat(authorService.getAll(0, 3, SortField.NAME, SortOrder.ASC, Optional.empty()))
            .extracting(Author::name).contains("John", "Müller", "müller");

        /*
         * Page 1
         */
        PageRequest pageRequest2 = PageRequest.of(1, 3).withSort(Sort.by("name").ascending());
        PageImpl<AuthorEntity> page2 = new PageImpl<>(
            List.of(
                AuthorEntity4,
                AuthorEntity5,
                AuthorEntity6
            ),
            pageRequest2,
            3L
        );
        when(authorRepository.findAll(pageRequest2))
            .thenReturn(page2);

        // act
        assertThat(authorService.getAll(1, 3, SortField.NAME, SortOrder.ASC, Optional.empty()))
            .extracting(Author::name).contains("Meier", "Rein", "Weg");

        /*
         * Page 2
         */
        PageRequest pageRequest3 = PageRequest.of(3, 2).withSort(Sort.by("name").ascending());
        PageImpl<AuthorEntity> page3 = new PageImpl<>(
            List.of(
                AuthorEntity7,
                AuthorEntity8
            ),
            pageRequest3,
            2L
        );
        when(authorRepository.findAll(pageRequest3))
            .thenReturn(page3);

        // act
        assertThat(authorService.getAll(3, 2, SortField.NAME, SortOrder.ASC, Optional.empty()))
            .extracting(Author::name).contains("Frank", "FNG");
    }


    @Test
    void paginationTestWithFiltering() {

        when(authorRepository.findByName(eq("John"), any(Pageable.class)))
            .thenReturn(List.of(authorEntity1));

        // act
        assertThat(authorService.getAll(0, 2, SortField.NAME, SortOrder.ASC, Optional.of("John")))
            .extracting(Author::name).contains("John");
    }

    @ParameterizedTest
    @ValueSource(strings = {"John", "john", "JOHn", "  John ", "\njohn\t"})
    void paginationTestWithFiltering2(String authorName) {
        PageRequest pageRequest = PageRequest.of(0, 11).withSort(Sort.by("id").ascending());
        when(authorRepository.findByName(authorName.trim(), pageRequest))
            .thenReturn(List.of(authorEntity1));

        // act
        assertThat(authorService.getAll(0, 11, SortField.ID, SortOrder.ASC, Optional.of(authorName)))
            .extracting(Author::name).contains("John");

        verify(authorRepository).findByName(authorName.trim(), pageRequest);
        verifyNoMoreInteractions(authorRepository);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "   ", "\t", " \n "})
    void test_empty(String authorName) {
        PageRequest pageRequest1 = PageRequest.of(0, 3).withSort(Sort.by("name").ascending());
        PageImpl<AuthorEntity> page1 = new PageImpl<>(
            List.of(
                authorEntity1,
                AuthorEntity2,
                AuthorEntity3)
            , pageRequest1
            , 3L
        );

        when(authorRepository.findAll(pageRequest1)).thenReturn(page1);

        // act + assert
        assertThat(authorService.getAll(0, 3, SortField.NAME, SortOrder.ASC, Optional.empty()))
            .extracting(Author::name).contains("John", "Müller", "müller");
    }

    @Test
    void allAuthors_nameNotFound() {
        when(authorRepository.findByName("steve", PageRequest.of(0, 11).withSort(Sort.by("id").ascending())))
            .thenReturn(List.of());

        // act + assert
        assertThat(authorService.getAll(0, 11, SortField.ID, SortOrder.ASC, Optional.of("steve")))
            .isEmpty();
        //
        verify(authorRepository).findByName("steve", PageRequest.of(0, 11).withSort(Sort.by("id").ascending()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Meier", "meier", "mEier", "  Meier ", "\nMeier\t"})
    void allAuthors_authorFound(String authorName) {
        when(authorRepository.findByName(authorName.trim(), PageRequest.of(0, 11).withSort(Sort.by("name").ascending())))
            .thenReturn(List.of(AuthorEntity4));

        // act + assert
        assertThat(authorService.getAll(0, 11, SortField.NAME, SortOrder.ASC, Optional.of(authorName)))
            .hasSize(1)
            .extracting(Author::name).containsExactly("Meier");
        assertThat(authorService.getAll(6, 9, SortField.NAME, SortOrder.ASC, Optional.of(authorName)))
            .isEmpty();
    }

    @Test
    void allAuthors_authorFoundTwice() {

        when(authorRepository.findByName("Müller", PageRequest.of(0, 11, Sort.by("name").ascending())))
            .thenReturn(List.of(AuthorEntity2, AuthorEntity3));

        // act + assert
        assertThat(authorService.getAll(0, 11, SortField.NAME, SortOrder.ASC, Optional.of("Müller")))
            .hasSize(2)
            .extracting(Author::name).containsExactlyInAnyOrder("Müller", "müller");
    }
}
