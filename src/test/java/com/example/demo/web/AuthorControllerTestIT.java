package com.example.demo.web;

import com.example.demo.model.Author;
import com.example.demo.model.AuthorsEnvelopDto;
import com.example.demo.model.enums.SortOrder;
import com.example.demo.repository.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorControllerTestIT {

    @LocalServerPort
    int randomServerPort;
    @Autowired
    private AuthorRepository authorRepository;
    private URI uri;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() throws URISyntaxException {
        uri = new URI("http://localhost:" + randomServerPort + "/api/authors");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    }

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void test_addAuthor() {
        // preparation

        Author author = new Author(30L, "steve", "france", LocalDate.of(1985, 4, 15));

        // act
        ResponseEntity<Author> responseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(author, headers), Author.class);

        // assert
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody()).satisfies(createdAuthor -> {
                assertThat(createdAuthor.name()).isEqualTo("steve");
                assertThat(createdAuthor.country()).isEqualTo("france");
                assertThat(createdAuthor.birthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
            });
        });
    }

    @Test
    void test_validAuthor_success() throws JsonProcessingException {
        // preparation
        Map<String, String> authorAsMap = Map.of("name", "steve", "country", "France", "birthDate", "2000-04-13");

        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(authorAsMap);

        // act
        ResponseEntity<Author> responseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(valueAsString, headers), Author.class);

        // assert
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody()).satisfies(createdAuthor -> {
                assertThat(createdAuthor.name()).isEqualTo("steve");
                assertThat(createdAuthor.country()).isEqualTo("France");
                assertThat(createdAuthor.birthDate()).isEqualTo(LocalDate.of(2000, 4, 13));
            });
        });
    }

    @Test
    void test_validAuthor_missingName() throws JsonProcessingException {
        // preparation
        Map<String, String> authorAsMap = Map.of("country", "France", "birthDate", "2000-04-13");

        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(authorAsMap);

        // act
        ParameterizedTypeReference<Map<String, String>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(valueAsString, headers), responseType);

        // assert
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody()).satisfies(errorMessage -> assertThat(errorMessage.get("name")).isEqualTo("'name' can not be empty"));
        });
    }

    @Test
    void test_validAuthor_missingCountry() throws JsonProcessingException {
        // preparation
        Map<String, String> authorAsMap = Map.of("name", "steve", "birthDate", "2000-04-13");

        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(authorAsMap);

        // act
        ParameterizedTypeReference<Map<String, String>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(valueAsString, headers), responseType);

        // assert
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody()).satisfies(errorMessage -> assertThat(errorMessage.get("country")).isEqualTo("'country' can not be empty"));
        });
    }

    @Test
    void test_validAuthor_missingBirthDate() throws JsonProcessingException {
        // preparation
        Map<String, String> authorAsMap = Map.of("name", "steve", "country", "France");

        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(authorAsMap);

        // act
        ParameterizedTypeReference<Map<String, String>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(valueAsString, headers), responseType);

        // assert
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody()).satisfies(errorMessage -> assertThat(errorMessage.get("birthDate")).isEqualTo("'birthDate' can not be empty"));
        });
    }

    @Test
    void test_validAuthor_missingSeveralElements() throws JsonProcessingException {
        // preparation
        Map<String, String> authorAsMap = Map.of(
            "name", "Land",
            "birthDate", "2000-04-13");

        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(authorAsMap);

        // act
        ParameterizedTypeReference<Map<String, String>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(valueAsString, headers), responseType);

        // assert
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody()).satisfies(errorMessage -> assertThat(errorMessage.get("country")).isEqualTo("'country' can not be empty"));
        });
    }

    @Test
    void test_getAllAuthors() {

        // preparation
        Author author = new Author(1L, "steve", "france", LocalDate.of(1985, 4, 15));
        assertThat(restTemplate.postForEntity(uri, new HttpEntity<>(author, headers), Author.class))
            .satisfies(authorResponseEntity -> assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK));

        // act
        ResponseEntity<AuthorsEnvelopDto> responseEntity =
            restTemplate.exchange(uri.toString() + "?pageNumber=0&pageSize=10", HttpMethod.GET, new HttpEntity<>("", headers), AuthorsEnvelopDto.class);

        //
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody())
                .isNotNull()
                .satisfies(authorsEnvelopDto -> {
                    assertThat(authorsEnvelopDto.pageNumber()).isEqualTo(0);
                    assertThat(authorsEnvelopDto.pageSize()).isEqualTo(10);
                    assertThat(authorsEnvelopDto.authorsCount()).isEqualTo(1L);
                    assertThat(authorsEnvelopDto.sortOrder()).isEqualTo(SortOrder.ASC);
                    assertThat(authorsEnvelopDto.sortField()).isEqualTo(AuthorsEnvelopDto.SortField.NAME);
                    assertThat(authorsEnvelopDto.authors()).satisfies(author1 -> {
                        assertThat(author1.id()).isNotEqualTo(1L);
                        assertThat(author1.name()).isEqualTo("steve");
                        assertThat(author1.country()).isEqualTo("france");
                        assertThat(author1.birthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
                    }, atIndex(0));
                });
        });
    }

    @Test
    void test_getAllAuthors_invalid_pageNumber() {

        Author author = new Author(1L, "steve", "france", LocalDate.of(1985, 4, 15));
        ResponseEntity<Author> authorResponseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(author, headers), Author.class);
        assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Act
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            uri + "?pageNumber=-1&pageSize=10",
            HttpMethod.GET,
            new HttpEntity<>("", headers),
            responseType
        );

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
        assertThat(responseEntity.getBody()).contains("Invalid argument");
        assertThat(responseEntity.getBody()).contains("Parameter pageNumber must be greater or equal 0");
    }

    @Test
    void test_getAllAuthors_invalid_pageSize() {

        Author author = new Author(1L, "steve", "france", LocalDate.of(1985, 4, 15));
        ResponseEntity<Author> authorResponseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(author, headers), Author.class);
        assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Act
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            uri + "?pageNumber=0&pageSize=0",
            HttpMethod.GET,
            new HttpEntity<>("", headers),
            responseType
        );

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
        assertThat(responseEntity.getBody()).contains("Invalid argument");
        assertThat(responseEntity.getBody()).contains("Parameter pageSize must be greater or equal 1");
    }

    @Test
    void test_getAllAuthors_invalid_pageSize_2() {

        Author author = new Author(1L, "steve", "france", LocalDate.of(1985, 4, 15));
        ResponseEntity<Author> authorResponseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(author, headers), Author.class);
        assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Act
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            uri + "?pageNumber=0&pageSize=2000",
            HttpMethod.GET,
            new HttpEntity<>("", headers),
            responseType
        );

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
        assertThat(responseEntity.getBody()).contains("Invalid argument");
        assertThat(responseEntity.getBody()).contains("Parameter pageSize must be less than 1000");
    }

    @Test
    void test_getAllAuthors_invalid_pageSize_and_pageNumber() {

        Author author = new Author(1L, "steve", "france", LocalDate.of(1985, 4, 15));
        ResponseEntity<Author> authorResponseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(author, headers), Author.class);
        assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Act
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            uri + "?pageNumber=-1&pageSize=-2",
            HttpMethod.GET,
            new HttpEntity<>("", headers),
            responseType
        );

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
        assertThat(responseEntity.getBody()).contains("Invalid argument");
        assertThat(responseEntity.getBody()).contains("Parameter pageSize must be greater or equal 1");
        assertThat(responseEntity.getBody()).contains("Parameter pageNumber must be greater or equal 0");
    }


    @Test
    void test_getAuthor() {
        // preparation
        Author author = new Author(1L, "steve", "france", LocalDate.of(1985, 4, 15));
        Author createdAuthor = restTemplate.postForEntity(uri, new HttpEntity<>(author, headers), Author.class).getBody();
        assertThat(createdAuthor).isNotNull();

        // act
        ResponseEntity<Author> responseEntity = restTemplate.getForEntity("/api/authors/" + createdAuthor.id(), Author.class);

        //
        assertThat(responseEntity).satisfies(authorResponseEntity -> {
            assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(authorResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(authorResponseEntity.getBody()).satisfies(foundAuthor -> {
                assertThat(foundAuthor.id()).isNotEqualTo(1L);
                assertThat(foundAuthor.name()).isEqualTo("steve");
                assertThat(foundAuthor.country()).isEqualTo("france");
                assertThat(foundAuthor.birthDate()).isEqualTo(LocalDate.of(1985, 4, 15));
            });
        });
    }

    @Test
    void test_removeAuthor() {
        // preparation
        Author author = new Author(1L, "steve", "france", LocalDate.of(1985, 4, 15));
        Author createdAuthor = restTemplate.postForEntity(uri, new HttpEntity<>(author, headers), Author.class).getBody();
        assertThat(createdAuthor).isNotNull();

        // act
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/api/authors/" + createdAuthor.id(), HttpMethod.DELETE, new HttpEntity<>("", headers), Void.class);
        //assert
        assertThat(responseEntity).satisfies(authorResponseEntity -> assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK));
    }

    @Test
    void test_updateAuthor() {
        // preparation
        Author author = new Author(1L, "steve", "france", LocalDate.of(1985, 4, 15));
        Author createdAuthor = restTemplate.postForEntity(uri, new HttpEntity<>(author, headers), Author.class).getBody();
        assertThat(createdAuthor).isNotNull();
        Author updatedAuthor = new Author(1L, "steve", "france", LocalDate.of(1981, 4, 15));

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
