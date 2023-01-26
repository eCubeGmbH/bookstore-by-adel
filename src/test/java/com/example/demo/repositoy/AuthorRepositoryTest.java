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

    @ParameterizedTest
    @ValueSource(strings = {"Name", "nAme", "namE", "nAMe"})
    void test_findByName(String name) {

        String uuid = UUID.randomUUID().toString();
        AuthorEntity author = new AuthorEntity(uuid, "name", "country", LocalDate.of(1997, 1, 11));
        authorRepository.save(author);

        assertThat(authorRepository.findByName(name))
                .isNotEmpty()
                .hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name", "nAme", "namE", "nAMe"})
    void test_findByNameNative(String name) {

        String uuid = UUID.randomUUID().toString();
        AuthorEntity author = new AuthorEntity(uuid, "name", "country", LocalDate.of(1997, 1, 11));
        authorRepository.save(author);

        assertThat(authorRepository.findByNameNative(name))
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void test_findByNameSorting() {
        String uuid = "0000";
        String uuid1 = "2222";
        String uuid2 = "1111";
        String uuid3 = "4444";
        AuthorEntity author = new AuthorEntity(uuid, "adel", "country", LocalDate.of(1999, 1, 11));
        AuthorEntity author1 = new AuthorEntity(uuid1, "adel", "country", LocalDate.of(1998, 1, 11));
        AuthorEntity author2= new AuthorEntity(uuid2, "adel", "country", LocalDate.of(1997, 1, 11));
        AuthorEntity author3= new AuthorEntity(uuid3, "sven", "country", LocalDate.of(1997, 1, 11));
        authorRepository.save(author);
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);

        Sort sortOrder = Sort.by("name").ascending().and(Sort.by("id").ascending());

        assertThat(authorRepository.findByName("adel", sortOrder))
                .isNotEmpty()
                .hasSize(3)
                .extracting(AuthorEntity::getId).containsExactly(uuid, uuid2, uuid1)
        ;
    }

    @Test
    void test_findByNamePageable() {
        String uuid = "0000";
        String uuid1 = "2222";
        String uuid2 = "1111";
        String uuid3 = "4444";
        AuthorEntity author = new AuthorEntity(uuid, "adel", "country", LocalDate.of(1999, 1, 11));
        AuthorEntity author1 = new AuthorEntity(uuid1, "adel", "country", LocalDate.of(1998, 1, 11));
        AuthorEntity author2= new AuthorEntity(uuid2, "adel", "country", LocalDate.of(1997, 1, 11));
        AuthorEntity author3= new AuthorEntity(uuid3, "sven", "country", LocalDate.of(1997, 1, 11));
        authorRepository.save(author);
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);

        Sort sortOrder = Sort.by("name").ascending()
                .and(Sort.by("id").ascending());

        // page 1
        PageRequest pageRequest = PageRequest.of(0, 2).withSort(sortOrder);
        assertThat(authorRepository.findByName("adel", pageRequest))
                .isNotEmpty()
                .hasSize(2)
                .extracting(AuthorEntity::getId).containsExactly(uuid, uuid2)
        ;

        // page 2
        pageRequest = PageRequest.of(1, 2).withSort(sortOrder);
        assertThat(authorRepository.findByName("adel", pageRequest))
                .isNotEmpty()
                .hasSize(1)
                .extracting(AuthorEntity::getId).containsExactly(uuid1)
        ;
    }
    @Test
    void test_findPageable() {
        String uuid = "0000";
        String uuid1 = "2222";
        String uuid2 = "1111";
        String uuid3 = "4444";
        AuthorEntity author = new AuthorEntity(uuid, "adel", "country", LocalDate.of(1999, 1, 11));
        AuthorEntity author1 = new AuthorEntity(uuid1, "adel", "country", LocalDate.of(1998, 1, 11));
        AuthorEntity author2 = new AuthorEntity(uuid2, "adel", "country", LocalDate.of(1997, 1, 11));
        AuthorEntity author3 = new AuthorEntity(uuid3, "sven", "country", LocalDate.of(1997, 1, 11));
        authorRepository.save(author);
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);

        Sort sortOrder = Sort.by("name").ascending()
                .and(Sort.by("id").ascending());

        // page 1
        PageRequest pageRequest = PageRequest.of(0, 2).withSort(sortOrder);
        assertThat(authorRepository.findAll(pageRequest))
                .isNotEmpty()
                .hasSize(2)
                .extracting(AuthorEntity::getId).containsExactly(uuid, uuid2)
        ;

        // page 2
        pageRequest = PageRequest.of(1, 2).withSort(sortOrder);
        assertThat(authorRepository.findAll(pageRequest))
                .isNotEmpty()
                .hasSize(2)
                .extracting(AuthorEntity::getId).containsExactly(uuid1, uuid3)
        ;

        // page 3
        pageRequest = PageRequest.of(2, 2).withSort(sortOrder);
        assertThat(authorRepository.findAll(pageRequest))
                .isEmpty()
        ;
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
