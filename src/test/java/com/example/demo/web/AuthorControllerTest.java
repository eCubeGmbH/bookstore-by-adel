package com.example.demo.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    @Mock
    private AuthorRepository repository;
    @InjectMocks
    private AuthorController controller;


    @Test
    void addAuthor() {
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));

        Mockito.when(repository.addAuthor(author)).thenReturn(author);

        assertThat(controller.addAuthor(author)).satisfies(createdAuthor -> {
            assertThat(createdAuthor.getId()).isEqualTo("ABC123");
            assertThat(createdAuthor.getName()).isEqualTo("steve");
            assertThat(createdAuthor.getCountry()).isEqualTo("france");
            assertThat(createdAuthor.getBirthDate()).isEqualTo(LocalDate.of(1985,4,15));
        });

        Mockito.verify(repository).addAuthor(author);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void getAllAuthors() {
        assertThat(controller.getAllAuthors())
                .isEmpty();

        controller.addAuthor(new Author("id", "name", "country", LocalDate.of(1998, 5, 11)));

        assertThat(controller.getAllAuthors())
                .isNotEmpty();
    }


    @Test
    void getAuthor() {
        controller.getAuthor(String.valueOf(new Author("XD123", "notail", "usa", LocalDate.of(1992,1,1))));

        Mockito.when(repository.getAuthor("XD123")).thenReturn(new Author());
        assertThat(controller.getAuthor("")).satisfies(createdAuthor -> {
        assertThat(controller.getAuthor(""));
        });
        Mockito.verify(repository).getAuthor("");
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void removeAuthor() {
    }

    @Test
    void updateAuthor() {
    }
}