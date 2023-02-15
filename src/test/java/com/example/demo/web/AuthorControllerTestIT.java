package com.example.demo.web;

import com.example.demo.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorControllerTestIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void test_addAuthor() throws URISyntaxException {
        // preparation
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));

        // act
        ResponseEntity<Author> responseEntity = restTemplate.postForEntity("/api/authors/", author, Author.class);

        // assert
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody()).satisfies(createdAuthor -> {
                assertThat(createdAuthor.id()).isNotEqualTo("ABC123");
                assertThat(createdAuthor.name()).isEqualTo("steve");
                assertThat(createdAuthor.country()).isEqualTo("france");
                assertThat(createdAuthor.birthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
            });
        });
    }

    @Test
    void test_getAllAuthors() {

        // preparation
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));
        assertThat(restTemplate.postForEntity("/api/authors/", author, Author.class))
                .satisfies(authorResponseEntity -> assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK));
        // act

        ResponseEntity<List<Author>> responseEntity = restTemplate.exchange("/api/authors?from=0&to=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        //
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody())
                    .isNotEmpty()
                    .hasSize(1)
                    .satisfies(author1 -> {
                        assertThat(author1.id()).isNotEqualTo("ABC123");
                        assertThat(author1.name()).isEqualTo("steve");
                        assertThat(author1.country()).isEqualTo("france");
                        assertThat(author1.birthDate()).isEqualTo(LocalDate.of(1985, 4, 15));

                    }, atIndex(0));
        });
    }

    @Test
    void test_getAuthor() {
        // preparation
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));
        Author createdAuthor = restTemplate.postForEntity("/api/authors/", author, Author.class).getBody();
        assertThat(createdAuthor).isNotNull();

        // act
        ResponseEntity<Author> responseEntity = restTemplate.getForEntity("/api/authors/" + createdAuthor.id(), Author.class);

        //
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody()).satisfies(foundAuthor -> {
                assertThat(foundAuthor.id()).isNotEqualTo("ABC123");
                assertThat(foundAuthor.name()).isEqualTo("steve");
                assertThat(foundAuthor.country()).isEqualTo("france");
                assertThat(foundAuthor.birthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
            });
        });
    }

    @Test
    void test_getAuthor_notFound() {
        // act
        ResponseEntity<Author> responseEntity = restTemplate.getForEntity("/api/authors/" + "blah", Author.class);

        //assert
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
        });
    }


    @Test
    void test_removeAuthor() {
        // preparation
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));
        Author createdAuthor = restTemplate.postForEntity("/api/authors/", author, Author.class).getBody();
        assertThat(createdAuthor).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // act
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/api/authors/" + createdAuthor.id(), HttpMethod.DELETE, new HttpEntity<>("", headers), Void.class);
        //assert
        assertThat(responseEntity).satisfies(authorResponseEntity -> assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK));
    }

    @Test
    void test_updateAuthor() {
        // preparation
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1985, 4, 15));
        Author createdAuthor = restTemplate.postForEntity("/api/authors/", author, Author.class).getBody();
        assertThat(createdAuthor).isNotNull();
        Author updatedAuthor = new Author("ABC123", "steve", "france", LocalDate.of(1981, 4, 15));

        //act
        ResponseEntity<Author> responseEntity = restTemplate.exchange("/api/authors/" + createdAuthor.id(), HttpMethod.PUT, new HttpEntity<>(updatedAuthor), Author.class);
        //assert
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody()).satisfies(responseAuthor -> assertThat(responseAuthor.birthDate()).isNotEqualTo(LocalDate.of(1985, 4, 15)));
        });
    }


}