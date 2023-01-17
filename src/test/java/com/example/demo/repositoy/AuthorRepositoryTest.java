package com.example.demo.repositoy;

import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.repository.AuthorRepository;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.bouncycastle.util.Strings;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void Test_addAuthor() {
        String uuid = UUID.randomUUID().toString();
        AuthorEntity author = new AuthorEntity(uuid, "name", "country", LocalDate.of(1997, 1, 1));

        assertThat(authorRepository.save(author)).satisfies(addedAuthor -> {
            assertThat(addedAuthor.getId()).isEqualTo(uuid);
            assertThat(addedAuthor.getName()).isEqualTo("name");
            assertThat(addedAuthor.getCountry()).isEqualTo("country");
            assertThat(addedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));
        });
    }

    @Test
    void Test_getAllAuthors() {

        String uuid = UUID.randomUUID().toString();
        AuthorEntity author = new AuthorEntity(uuid, "name", "country", LocalDate.of(1997, 1, 1));
        authorRepository.save(author);

        assertThat(authorRepository.findAll())
                .isNotEmpty()
                .hasSize(1)
                .satisfies(author1 -> {
                    assertThat(author1.getId()).isEqualTo(uuid);
                    assertThat(author1.getName()).isEqualTo("name");
                    assertThat(author1.getCountry()).isEqualTo("country");
                    assertThat(author1.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));

                }, Index.atIndex(0));
    }

    @Test
    void test_findByNameIgnoreCase() {

        String uuid = UUID.randomUUID().toString();
        AuthorEntity author = new AuthorEntity(uuid, "name", "country", LocalDate.of(1997, 1, 11));
        authorRepository.save(author);

        assertThat(authorRepository.findByNameIgnoreCase("NaMe"))
                .isNotEmpty()
                .hasSize(1);

    }

    @ParameterizedTest
    @ValueSource(strings = {"Name", "nAme", "namE", "nAMe"})
    void test_findByNameIgnoreCase2(String name) {
        String uuid = UUID.randomUUID().toString();
        AuthorEntity author = new AuthorEntity(uuid, "name", "country", LocalDate.of(1977, 1, 22));
        authorRepository.save(author);

        assertThat(authorRepository.findByNameIgnoreCase(name))
                .isNotEmpty()
                .hasSize(1);
    }


    @Test
    void test_getAuthor() {
        String uuid = UUID.randomUUID().toString();
        AuthorEntity author = new AuthorEntity(uuid, "name", "country", LocalDate.of(1997, 1, 1));
        authorRepository.save(author);

        assertThat(authorRepository.findById(uuid))
                .isPresent()
                .get().satisfies(foundAuthor -> {
                    assertThat(foundAuthor.getId()).isEqualTo(uuid);
                    assertThat(foundAuthor.getName()).isEqualTo("name");
                    assertThat(foundAuthor.getCountry()).isEqualTo("country");
                    assertThat(foundAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));
                });
    }

    @Test
    void Test_removeAuthor() {
        String uuid = UUID.randomUUID().toString();
        AuthorEntity author = new AuthorEntity(uuid, "name", "country", LocalDate.of(1997, 1, 1));
        authorRepository.save(author);

        authorRepository.delete(author);
    }

    @Test
    void Test_updateAuthor() {
        String uuid = UUID.randomUUID().toString();
        AuthorEntity author = new AuthorEntity(uuid, "name", "country", LocalDate.of(1997, 1, 1));
        authorRepository.save(author);

        author.setBirthDate(LocalDate.of(1997, 12, 24));

        assertThat(authorRepository.save(author))
                .satisfies(updatedAuthor -> {
                    assertThat(updatedAuthor.getId()).isEqualTo(uuid);
                    assertThat(updatedAuthor.getName()).isEqualTo("name");
                    assertThat(updatedAuthor.getCountry()).isEqualTo("country");
                    assertThat(updatedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 12, 24));
                });
    }
}
