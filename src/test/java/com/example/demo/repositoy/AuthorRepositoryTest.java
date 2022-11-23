package com.example.demo.repositoy;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorRepositoryTest {
    @Test
    void Test_addAuthor() {
        AuthorRepository repositoryMap = new AuthorRepository();
        Author author = new Author("id", "name", "country", LocalDate.of(1997, 1, 1));

        assertThat(repositoryMap.addAuthor(author)).satisfies(addedAuthor -> {
            assertThat(addedAuthor.getId()).isEqualTo("id");
            assertThat(addedAuthor.getName()).isEqualTo("name");
            assertThat(addedAuthor.getCountry()).isEqualTo("country");
            assertThat(addedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));
        });
    }

    @Test
    void Test_getAllAuthors() {
        AuthorRepository repositoryMap = new AuthorRepository();
        Author author = new Author("id", "name", "country", LocalDate.of(1997, 1, 1));
        repositoryMap.addAuthor(author);

        assertThat(repositoryMap.getAll())
                .isNotEmpty()
                .hasSize(1)
                .satisfies(author1 -> {
                    assertThat(author1.getId()).isEqualTo("id");
                    assertThat(author1.getName()).isEqualTo("name");
                    assertThat(author1.getCountry()).isEqualTo("country");
                    assertThat(author1.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));

                }, Index.atIndex(0));
    }

    @Test
    void Test_getAuthor() {
        AuthorRepository repositoryMap = new AuthorRepository();
        Author author = new Author("id", "name", "country", LocalDate.of(1997, 1, 1));
        Author addedAuthor = repositoryMap.addAuthor(author);

        assertThat(repositoryMap.getAuthor(addedAuthor.getId())).satisfies(foundAuthor -> {
            assertThat(foundAuthor.getId()).isEqualTo("id");
            assertThat(foundAuthor.getName()).isEqualTo("name");
            assertThat(foundAuthor.getCountry()).isEqualTo("country");
            assertThat(foundAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));
        });
    }

    @Test
    void Test_removeAuthor() {
        AuthorRepository repositoryMap = new AuthorRepository();
        Author author = new Author("id", "name", "country", LocalDate.of(1997, 1, 1));
        Author addAuthor = repositoryMap.addAuthor(author);

        repositoryMap.deleteAuthor(addAuthor);
    }

    @Test
    void Test_updateAuthor() {
        AuthorRepository repositoryMap = new AuthorRepository();
        Author updateAuthor = new Author("id", "name", "country", LocalDate.of(1997, 1, 5));

        assertThat(repositoryMap.updateAuthor(updateAuthor))
                .satisfies(updatedAuthor -> {
                    assertThat(updatedAuthor.getId()).isEqualTo("id");
                    assertThat(updatedAuthor.getName()).isEqualTo("name");
                    assertThat(updatedAuthor.getCountry()).isEqualTo("country");
                    assertThat(updatedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 5));
                });
    }
}