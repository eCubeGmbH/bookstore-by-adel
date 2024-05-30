package com.example.demo.repositoy;

import com.example.demo.model.entity.AuthorEntity;
import org.assertj.core.api.Assertions;
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
import java.util.List;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class AuthorRepoTestIT {

    @Autowired
    private TestAuthorRepository testAuthorRepository;

    @Test
    public void testFindByNameIgnoreCase() {
        AuthorEntity author = new AuthorEntity("Steve", "France", LocalDate.of(1985, 4, 15), new ArrayList<>());
        testAuthorRepository.save(author);

        // When
        List<AuthorEntity> authors = testAuthorRepository.findByNameIgnoreCase("steve");

        // Then
        assertThat(authors).hasSize(1);
        assertThat(authors.get(0).getName()).isEqualTo("Steve");
    }

    @Test
    public void testFindByName() {
        AuthorEntity author = new AuthorEntity("Steve", "France", LocalDate.of(1985, 4, 15), new ArrayList<>());
        testAuthorRepository.save(author);

        // When
        List<AuthorEntity> authors = testAuthorRepository.findByName("Steve", Sort.by(Sort.Order.asc("name")));

        // Then
        assertThat(authors).hasSize(1);
        assertThat(authors.get(0).getName()).isEqualTo("Steve");
    }

    @Test
    public void testFindByNameNative() {
        AuthorEntity author = new AuthorEntity("Steve", "France", LocalDate.of(1985, 4, 15), new ArrayList<>());
        testAuthorRepository.save(author);

        // When
        List<AuthorEntity> authors = testAuthorRepository.findByNameNative("steve");

        // Then
        assertThat(authors).hasSize(1);
        assertThat(authors.get(0).getName()).isEqualTo("Steve");
    }

    @Test
    public void testFindByNameWithSort() {
        AuthorEntity author1 = new AuthorEntity("Steve", "France", LocalDate.of(1985, 4, 15), new ArrayList<>());
        AuthorEntity author2 = new AuthorEntity("Alice", "Germany", LocalDate.of(1990, 8, 25), new ArrayList<>());
        testAuthorRepository.save(author1);
        testAuthorRepository.save(author2);

        // When
        List<AuthorEntity> authors = testAuthorRepository.findByName("Steve", Sort.by(Sort.Order.asc("name")));

        // Then
        assertThat(authors).hasSize(1);
        assertThat(authors.get(0).getName()).isEqualTo("Steve");
    }

    @Test
    public void testFindByNameWithPagination() {
        AuthorEntity author1 = new AuthorEntity("Steve", "France", LocalDate.of(1985, 4, 15), new ArrayList<>());
        AuthorEntity author2 = new AuthorEntity("Alice", "Germany", LocalDate.of(1990, 8, 25), new ArrayList<>());
        testAuthorRepository.save(author1);
        testAuthorRepository.save(author2);

        // When
        List<AuthorEntity> authors = testAuthorRepository.findByName("Steve", PageRequest.of(0, 10));

        // Then
        assertThat(authors).hasSize(1);
        assertThat(authors.get(0).getName()).isEqualTo("Steve");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name", "nAme", "namE", "nAMe"})
    void test_findByNameNative(String name) {

        AuthorEntity author = new AuthorEntity("name", "country", LocalDate.of(1997, 1, 11), new ArrayList<>());
        testAuthorRepository.save(author);
        // Then
        assertThat(testAuthorRepository.findByNameNative(name))
            .isNotEmpty()
            .hasSize(1);
    }

    @Test
    void test_findByNameIgnoreCase() {

        AuthorEntity author = new AuthorEntity("name", "country", LocalDate.of(1997, 1, 11), new ArrayList<>());
        testAuthorRepository.save(author);
        // Then
        assertThat(testAuthorRepository.findByNameIgnoreCase("NaMe"))
            .isNotEmpty()
            .hasSize(1);

    }

    @ParameterizedTest
    @ValueSource(strings = {"Name", "nAme", "namE", "nAMe"})
    void test_findByNameIgnoreCase2(String name) {
        AuthorEntity author = new AuthorEntity("name", "country", LocalDate.of(1977, 1, 22), new ArrayList<>());
        testAuthorRepository.save(author);
        // Then
        assertThat(testAuthorRepository.findByNameIgnoreCase(name))
            .isNotEmpty()
            .hasSize(1);
    }

    @Test
    void test_findByNameSorting() {
        AuthorEntity author = new AuthorEntity("adel", "country", LocalDate.of(1999, 1, 11), new ArrayList<>());
        AuthorEntity author1 = new AuthorEntity("adel", "country", LocalDate.of(1998, 1, 11), new ArrayList<>());
        AuthorEntity author2 = new AuthorEntity("adel", "country", LocalDate.of(1997, 1, 11), new ArrayList<>());
        AuthorEntity author3 = new AuthorEntity("sven", "country", LocalDate.of(1997, 1, 11), new ArrayList<>());
        testAuthorRepository.save(author);
        testAuthorRepository.save(author1);
        testAuthorRepository.save(author2);
        testAuthorRepository.save(author3);

        Sort sortOrder = Sort.by("name").ascending().and(Sort.by("id").ascending());
        PageRequest pageRequest = PageRequest.of(0, 4, sortOrder);
        Assertions.assertThat(testAuthorRepository.findByName("adel", pageRequest))
            .isNotEmpty()
            .hasSize(3)
            .extracting(AuthorEntity::getName).containsExactly("adel", "adel", "adel")
        ;
    }

}