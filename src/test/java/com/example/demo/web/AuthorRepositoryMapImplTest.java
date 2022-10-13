package com.example.demo.web;

import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorRepositoryMapImplTest {
    @Test
    void Test_addAuthor() {
        AuthorRepositoryMapImpl repositoryMap = new AuthorRepositoryMapImpl();
        Author author = new Author("id", "name", "country", LocalDate.of(1997, 1, 1));

        assertThat(repositoryMap.addAuthor(author)).satisfies(addedAuthor -> {
            assertThat(addedAuthor.getId()).isNotEqualTo("id");
            assertThat(addedAuthor.getName()).isEqualTo("name");
            assertThat(addedAuthor.getCountry()).isEqualTo("country");
            assertThat(addedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));

        });

    }

    @Test
    void Test_getAllAuthors() {
        AuthorRepositoryMapImpl repositoryMap = new AuthorRepositoryMapImpl();
        Author author = new Author("id", "name", "country", LocalDate.of(1997, 1, 1));
        repositoryMap.addAuthor(author);

        assertThat(repositoryMap.getAll())
                .isNotEmpty()
                .hasSize(1)
                .satisfies(author1 -> {
                    assertThat(author1.getId()).isNotEqualTo("id");
                    assertThat(author1.getName()).isEqualTo("name");
                    assertThat(author1.getCountry()).isEqualTo("country");
                    assertThat(author1.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));

                }, Index.atIndex(0));
    }

    @Test
    void Test_getAuthor() {
        AuthorRepositoryMapImpl repositoryMap = new AuthorRepositoryMapImpl();
        Author author = new Author("id", "name", "country", LocalDate.of(1997, 1, 1));
        Author addedAuthor = repositoryMap.addAuthor(author);


        assertThat(repositoryMap.getAuthor(addedAuthor.getId())).satisfies(foundAuthor -> {
            assertThat(foundAuthor.getId()).isNotEqualTo("id");
            assertThat(foundAuthor.getName()).isEqualTo("name");
            assertThat(foundAuthor.getCountry()).isEqualTo("country");
            assertThat(foundAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));

        });
    }

    @Test
    void Test_removeAuthor() {
        AuthorRepositoryMapImpl repositoryMap = new AuthorRepositoryMapImpl();
        Author author = new Author("id", "name", "country", LocalDate.of(1997, 1, 1));
        Author addAuthor = repositoryMap.addAuthor(author);

        repositoryMap.deleteAuthor(addAuthor.getId());

    }

    @Test
    void Test_updateAuthor() {
        AuthorRepositoryMapImpl repositoryMap = new AuthorRepositoryMapImpl();
        Author author = new Author("id", "name", "country", LocalDate.of(1997, 1, 1));
        Author addAuthor = repositoryMap.addAuthor(author);
        Author updateAuthor = new Author("id", "name", "country", LocalDate.of(1997, 1, 5));

        assertThat(repositoryMap.updateAuthor(addAuthor.getId(), updateAuthor))
                .satisfies(updatedAuthor -> {
                    assertThat(updatedAuthor.getId()).isNotEqualTo("id");
                    assertThat(updatedAuthor.getName()).isEqualTo("name");
                    assertThat(updatedAuthor.getCountry()).isEqualTo("country");
                    assertThat(updatedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 5));

                });

    }
}