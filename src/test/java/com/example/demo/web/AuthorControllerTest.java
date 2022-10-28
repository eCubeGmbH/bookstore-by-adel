package com.example.demo.web;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    @Mock
    private AuthorRepository repository;
    @InjectMocks
    private AuthorController controller;


    @Test
    void addAuthor() {
        // prep
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));

        // when
        Mockito.when(repository.addAuthor(author)).thenReturn(author);

        // act + assert
        assertThat(controller.addAuthor(author)).satisfies(createdAuthor -> {
            assertThat(createdAuthor.getId()).isEqualTo("ABC123");
            assertThat(createdAuthor.getName()).isEqualTo("steve");
            assertThat(createdAuthor.getCountry()).isEqualTo("france");
            assertThat(createdAuthor.getBirthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
        });

        // verify
        Mockito.verify(repository).addAuthor(author);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void getAllAuthors() {
        // prep
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));

        // when
        Mockito.when(repository.getAll()).thenReturn(List.of(author));

        // act + assert
        assertThat(controller.getAllAuthors())
                .isNotEmpty()
                .hasSize(1)
                .satisfies(createdAuthor -> {
                    assertThat(createdAuthor.getId()).isEqualTo("ABC123");
                    assertThat(createdAuthor.getName()).isEqualTo("steve");
                    assertThat(createdAuthor.getCountry()).isEqualTo("france");
                    assertThat(createdAuthor.getBirthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
                }, atIndex(0));

        // verify
        Mockito.verify(repository).getAll();
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void getAuthor() {
        // prep
        Author author = new Author("XD123", "steve", "france", LocalDate.of(1985, 4, 15));

        // when
        Mockito.when(repository.getAuthor("XD123")).thenReturn(author);

        // act + assert
        assertThat(controller.getAuthor("XD123")).satisfies(createdAuthor -> {
            assertThat(createdAuthor.getId()).isEqualTo("XD123");
            assertThat(createdAuthor.getName()).isEqualTo("steve");
            assertThat(createdAuthor.getCountry()).isEqualTo("france");
            assertThat(createdAuthor.getBirthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
        });

        // verify
        Mockito.verify(repository).getAuthor("XD123");
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void getAuthorNotFound() {
        // when
        Mockito.when(repository.getAuthor("XD123")).thenReturn(null);

        // act + assert
        assertThat(controller.getAuthor("XD123")).isNull();

        // verify
        Mockito.verify(repository).getAuthor("XD123");
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void removeAuthor() {
        // act
        controller.removeAuthor("FN123");

        Mockito.verify(repository).deleteAuthor("FN123");
    }

    @Test
    void updateAuthor() {
        Author author = new Author("FN123", "lara", "USA", LocalDate.of(1985, 4, 15));

        Mockito.when(repository.updateAuthor("FN123", author)).thenReturn(author);

        assertThat(controller.updateAuthor("FN123", author)).satisfies(updatedAuthor -> {
            assertThat(updatedAuthor.getId()).isEqualTo("FN123");
            assertThat(updatedAuthor.getName()).isEqualTo("lara");
            assertThat(updatedAuthor.getCountry()).isEqualTo("USA");
            assertThat(updatedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1985, 4, 15));

        });
    }
}