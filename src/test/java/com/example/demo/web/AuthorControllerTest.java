package com.example.demo.web;

import com.example.demo.model.Author;
import com.example.demo.model.AuthorsEnvelopDto;
import com.example.demo.model.enums.SortField;
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
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));

        // when
        when(authorService.addAuthor(author)).thenReturn(author);

        // act + assert
        assertThat(controller.addAuthor(author)).satisfies(createdAuthor -> {
            assertThat(createdAuthor.id()).isEqualTo("ABC123");
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
        Author author = new Author("AXX2213", "FM", "SWE", LocalDate.of(1877, 2, 1));
        List<Author> authors = List.of(author);
        AuthorsEnvelopDto envelope = new AuthorsEnvelopDto(1, 5, 1, SortField.NAME, SortOrder.ASC, null, authors);

        // when
        when(authorService.getAll(1, 5, SortField.NAME, SortOrder.ASC, Optional.empty())).thenReturn(envelope);

        // act + assert
        assertThat(controller.getAllAuthors(1, 5, SortField.NAME, SortOrder.ASC, Optional.empty())).satisfies(envelopeDto -> {
            assertThat(envelopeDto.pageNumber()).isEqualTo(1);
            assertThat(envelopeDto.pageSize()).isEqualTo(5);
            assertThat(envelopeDto.authorsCount()).isEqualTo(1);
            assertThat(envelopeDto.sortField()).isEqualTo(SortField.NAME);
            assertThat(envelopeDto.sortOrder()).isEqualTo(SortOrder.ASC);
            assertThat(envelopeDto.authors()).containsExactly(author);
        });

        // verify
        verify(authorService).getAll(1, 5, SortField.NAME, SortOrder.ASC, Optional.empty());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void getAllAuthors_emptyAuthorName() {
        // prep
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));
        List<Author> authors = List.of(author);
        AuthorsEnvelopDto envelope = new AuthorsEnvelopDto(0, 5, 1, SortField.ID, SortOrder.ASC, null, authors);

        // when
        when(authorService.getAll(0, 5, SortField.ID, SortOrder.ASC, Optional.empty())).thenReturn(envelope);

        // act + assert
        assertThat(controller.getAllAuthors(0, 5, SortField.ID, SortOrder.ASC, Optional.empty())).satisfies(envelopeDto -> {
            assertThat(envelopeDto.pageNumber()).isEqualTo(0);
            assertThat(envelopeDto.pageSize()).isEqualTo(5);
            assertThat(envelopeDto.authorsCount()).isEqualTo(1);
            assertThat(envelopeDto.sortField()).isEqualTo(SortField.ID);
            assertThat(envelopeDto.sortOrder()).isEqualTo(SortOrder.ASC);
            assertThat(envelopeDto.authors()).containsExactly(author);
        });

        // verify
        verify(authorService).getAll(0, 5, SortField.ID, SortOrder.ASC, Optional.empty());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void getAuthor() {
        // prep
        Author author = new Author("XD123", "steve", "france", LocalDate.of(1985, 4, 15));

        // when
        when(authorService.getAuthor("XD123")).thenReturn(author);

        // act + assert
        assertThat(controller.getAuthor("XD123")).satisfies(createdAuthor -> {
            assertThat(createdAuthor.id()).isEqualTo("XD123");
            assertThat(createdAuthor.name()).isEqualTo("steve");
            assertThat(createdAuthor.country()).isEqualTo("france");
            assertThat(createdAuthor.birthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
        });

        // verify
        verify(authorService).getAuthor("XD123");
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void getAuthorNotFound() {
        // when
        when(authorService.getAuthor("XD123")).thenReturn(null);

        // act + assert
        assertThat(controller.getAuthor("XD123")).isNull();

        // verify
        verify(authorService).getAuthor("XD123");
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void removeAuthor() {
        // act
        controller.removeAuthor("FN123");

        verify(authorService).deleteAuthor("FN123");
    }

    @Test
    void updateAuthor() {
        Author author = new Author("FN123", "lara", "USA", LocalDate.of(1985, 4, 15));

        when(authorService.updateAuthor("FN123", author)).thenReturn(author);

        assertThat(controller.updateAuthor("FN123", author)).satisfies(updatedAuthor -> {
            assertThat(updatedAuthor.id()).isEqualTo("FN123");
            assertThat(updatedAuthor.name()).isEqualTo("lara");
            assertThat(updatedAuthor.country()).isEqualTo("USA");
            assertThat(updatedAuthor.birthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
        });

        verify(authorService).updateAuthor("FN123", author);
        verifyNoMoreInteractions(authorService);
    }
}
