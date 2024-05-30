package com.example.demo.repositoy;

import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.repository.AuthorRepository;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class AuthorRepositoryTestIT {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void Test_addAuthor() {
        AuthorEntity author = new AuthorEntity("name", "country", LocalDate.of(1997, 1, 1), new ArrayList<>());

        assertThat(authorRepository.save(author)).satisfies(addedAuthor -> {
            assertThat(addedAuthor.getName()).isEqualTo("name");
            assertThat(addedAuthor.getCountry()).isEqualTo("country");
            assertThat(addedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));
        });
    }

    @Test
    void Test_getAllAuthors() {

        AuthorEntity author = new AuthorEntity("name", "country", LocalDate.of(1997, 1, 1), new ArrayList<>());
        authorRepository.save(author);

        assertThat(authorRepository.findAll()).isNotEmpty().hasSize(1).satisfies(author1 -> {
            assertThat(author1.getName()).isEqualTo("name");
            assertThat(author1.getCountry()).isEqualTo("country");
            assertThat(author1.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));

        }, Index.atIndex(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name", "nAme", "namE", "nAMe"})
    void test_findByName(String name) {

        AuthorEntity author = new AuthorEntity("name", "country", LocalDate.of(1997, 1, 11), new ArrayList<>());
        authorRepository.save(author);

        // page 1
        PageRequest pageRequest = PageRequest.of(0, 2).withSort(Sort.by("name").ascending().and(Sort.by("id").ascending()));

        assertThat(authorRepository.findByName(name, pageRequest)).isNotEmpty().hasSize(1);
    }


    @Test
    void test_findByNamePageable() {
        AuthorEntity author = new AuthorEntity("adel", "country", LocalDate.of(1999, 1, 11), new ArrayList<>());
        AuthorEntity author1 = new AuthorEntity("adel", "country", LocalDate.of(1998, 1, 11), new ArrayList<>());
        AuthorEntity author2 = new AuthorEntity("adel", "country", LocalDate.of(1997, 1, 11), new ArrayList<>());
        AuthorEntity author3 = new AuthorEntity("sven", "country", LocalDate.of(1997, 1, 11), new ArrayList<>());
        authorRepository.save(author);
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);

        Sort sortOrder = Sort.by("name").ascending().and(Sort.by("id").ascending());

        // page 1
        PageRequest pageRequest = PageRequest.of(0, 2).withSort(sortOrder);
        assertThat(authorRepository.findByName("adel", pageRequest))
            .hasSize(2);
        // page 2
        pageRequest = PageRequest.of(1, 2).withSort(sortOrder);
        assertThat(authorRepository.findByName("adel", pageRequest))
            .hasSize(1);
    }

    @Test
    void test_findPageable() {
        AuthorEntity author = new AuthorEntity("adel", "country", LocalDate.of(1999, 1, 11), new ArrayList<>());
        AuthorEntity author1 = new AuthorEntity("adel", "country", LocalDate.of(1998, 1, 11), new ArrayList<>());
        AuthorEntity author2 = new AuthorEntity("adel", "country", LocalDate.of(1997, 1, 11), new ArrayList<>());
        AuthorEntity author3 = new AuthorEntity("sven", "country", LocalDate.of(1997, 1, 11), new ArrayList<>());
        authorRepository.save(author);
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);

        Sort sortOrder = Sort.by("name").ascending().and(Sort.by("id").ascending());

        // page 1
        PageRequest pageRequest = PageRequest.of(0, 2).withSort(sortOrder);
        assertThat(authorRepository.findAll(pageRequest)).isNotEmpty().hasSize(2).extracting(AuthorEntity::getName).containsExactly("adel", "adel");

        // page 2
        pageRequest = PageRequest.of(1, 2).withSort(sortOrder);
        assertThat(authorRepository.findAll(pageRequest)).isNotEmpty().hasSize(2).extracting(AuthorEntity::getName).containsExactly("adel", "sven");

        // page 3
        pageRequest = PageRequest.of(2, 2).withSort(sortOrder);
        assertThat(authorRepository.findAll(pageRequest)).isEmpty();
    }

    @Test
    void test_getAuthor() {
        AuthorEntity author = new AuthorEntity("name", "country", LocalDate.of(1997, 1, 1), new ArrayList<>());
        AuthorEntity savedAuthor = authorRepository.save(author);

        assertThat(authorRepository.findById(savedAuthor.getId())).isPresent().get().satisfies(foundAuthor -> {
            assertThat(foundAuthor.getName()).isEqualTo("name");
            assertThat(foundAuthor.getCountry()).isEqualTo("country");
            assertThat(foundAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 1, 1));
        });
    }

    @Test
    void Test_removeAuthor() {
        AuthorEntity author = new AuthorEntity("name", "country", LocalDate.of(1997, 1, 1), new ArrayList<>());
        authorRepository.save(author);

        authorRepository.delete(author);
    }

    @Test
    void Test_updateAuthor() {
        AuthorEntity author = new AuthorEntity("name", "country", LocalDate.of(1997, 1, 1), new ArrayList<>());
        authorRepository.save(author);

        author.setBirthDate(LocalDate.of(1997, 12, 24));

        assertThat(authorRepository.save(author)).satisfies(updatedAuthor -> {
            assertThat(updatedAuthor.getName()).isEqualTo("name");
            assertThat(updatedAuthor.getCountry()).isEqualTo("country");
            assertThat(updatedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1997, 12, 24));
        });
    }
}
