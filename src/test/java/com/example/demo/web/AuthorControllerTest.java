package com.example.demo.web;

import com.example.demo.model.Author;
import com.example.demo.model.AuthorsEnvelopDto;
import com.example.demo.model.enums.SortOrder;
import com.example.demo.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController controller;

    @Test
    void addAuthor() {
        // prep
        Author author = new Author(1L, "steve", "france", LocalDate.of(1985, 4, 15));

        // when
        when(authorService.addAuthor(author)).thenReturn(author);

        // act + assert
        assertThat(controller.addAuthor(author)).satisfies(createdAuthor -> {
            assertThat(createdAuthor.id()).isEqualTo(1L);
            assertThat(createdAuthor.name()).isEqualTo("steve");
            assertThat(createdAuthor.country()).isEqualTo("france");
            assertThat(createdAuthor.birthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
        });

        // verify
        verify(authorService).addAuthor(author);
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void getAllAuthors_pagination() {
        Author author = new Author(2L, "FM", "SWE", LocalDate.of(1877, 2, 1));
        List<Author> authors = List.of(author);
        AuthorsEnvelopDto envelope = new AuthorsEnvelopDto(1, 5, 1, AuthorsEnvelopDto.SortField.NAME, SortOrder.ASC, null, authors);

        // when
        when(authorService.getAll(1, 5, AuthorsEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.empty())).thenReturn(envelope);

        // act + assert
        assertThat(controller.getAllAuthors(1, 5, AuthorsEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.empty())).satisfies(envelopeDto -> {
            assertThat(envelopeDto.pageNumber()).isEqualTo(1);
            assertThat(envelopeDto.pageSize()).isEqualTo(5);
            assertThat(envelopeDto.authorsCount()).isEqualTo(1);
            assertThat(envelopeDto.sortField()).isEqualTo(AuthorsEnvelopDto.SortField.NAME);
            assertThat(envelopeDto.sortOrder()).isEqualTo(SortOrder.ASC);
            assertThat(envelopeDto.authors()).containsExactly(author);
        });

        // verify
        verify(authorService).getAll(1, 5, AuthorsEnvelopDto.SortField.NAME, SortOrder.ASC, Optional.empty());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void getAllAuthors_emptyAuthorName() {
        // prep
        Author author = new Author(3L, "steve", "france", LocalDate.of(1985, 4, 15));
        List<Author> authors = List.of(author);
        AuthorsEnvelopDto envelope = new AuthorsEnvelopDto(0, 5, 1, AuthorsEnvelopDto.SortField.ID, SortOrder.ASC, null, authors);

        // when
        when(authorService.getAll(0, 5, AuthorsEnvelopDto.SortField.ID, SortOrder.ASC, Optional.empty())).thenReturn(envelope);

        // act + assert
        assertThat(controller.getAllAuthors(0, 5, AuthorsEnvelopDto.SortField.ID, SortOrder.ASC, Optional.empty())).satisfies(envelopeDto -> {
            assertThat(envelopeDto.pageNumber()).isEqualTo(0);
            assertThat(envelopeDto.pageSize()).isEqualTo(5);
            assertThat(envelopeDto.authorsCount()).isEqualTo(1);
            assertThat(envelopeDto.sortField()).isEqualTo(AuthorsEnvelopDto.SortField.ID);
            assertThat(envelopeDto.sortOrder()).isEqualTo(SortOrder.ASC);
            assertThat(envelopeDto.authors()).containsExactly(author);
        });

        // verify
        verify(authorService).getAll(0, 5, AuthorsEnvelopDto.SortField.ID, SortOrder.ASC, Optional.empty());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void getAuthor() {
        // prep
        Author author = new Author(4L, "steve", "france", LocalDate.of(1985, 4, 15));

        // when
        when(authorService.getAuthor(4L)).thenReturn(author);

        // act + assert
        assertThat(controller.getAuthor(4L)).satisfies(createdAuthor -> {
            assertThat(createdAuthor.id()).isEqualTo(4L);
            assertThat(createdAuthor.name()).isEqualTo("steve");
            assertThat(createdAuthor.country()).isEqualTo("france");
            assertThat(createdAuthor.birthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
        });

        // verify
        verify(authorService).getAuthor(4L);
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void getAuthorNotFound() {
        // when
        when(authorService.getAuthor(5L)).thenReturn(null);

        // act + assert
        assertThat(controller.getAuthor(5L)).isNull();

        // verify
        verify(authorService).getAuthor(5L);
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void removeAuthor() {
        // act
        controller.removeAuthor(6L);

        verify(authorService).deleteAuthor(6L);
    }

    @Test
    void updateAuthor() {
        Author author = new Author(7L, "lara", "USA", LocalDate.of(1985, 4, 15));

        when(authorService.updateAuthor(7L, author)).thenReturn(author);

        assertThat(controller.updateAuthor(7L, author)).satisfies(updatedAuthor -> {
            assertThat(updatedAuthor.id()).isEqualTo(7L);
            assertThat(updatedAuthor.name()).isEqualTo("lara");
            assertThat(updatedAuthor.country()).isEqualTo("USA");
            assertThat(updatedAuthor.birthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
        });

        verify(authorService).updateAuthor(7L, author);
        verifyNoMoreInteractions(authorService);
    }
}
