package com.example.demo.web;

import com.example.demo.model.Book;
import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
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
public class BookControllerTestIT {
    @LocalServerPort
    int randomServerPort;

    private URI uri;
    private HttpHeaders headers;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private AuthorEntity authorEntity;

    @BeforeEach
    void setUp() throws URISyntaxException {
        uri = new URI("http://localhost:" + randomServerPort + "/api/books");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        AuthorEntity authorEntity = new AuthorEntity("name", "usa", LocalDate.now());
        this.authorEntity = authorRepository.save(authorEntity);
    }

    @AfterEach
    void cleanUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void test_addBook() {
        // preparation
        Book book = new Book(1L, authorEntity.getId(), "National Math", LocalDate.of(1985, 4, 15));

        // act
        ResponseEntity<Book> responseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(book, headers), Book.class);

        // assert
        assertThat(responseEntity).satisfies(bookResponseEntity -> {
            assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(bookResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(bookResponseEntity.getBody()).satisfies(createdBook -> {
                assertThat(createdBook.name()).isEqualTo("National Math");
                assertThat(createdBook.publishDate()).isEqualTo(LocalDate.of(1985, 4, 15));
            });
        });
    }

    @Test
    void test_validBook_missingName() throws JsonProcessingException {
        // preparation
        Map<String, String> bookAsMap = Map.of("id", "12", "authorId", "" + authorEntity.getId(), "publishDate", "2000-04-13");

        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(bookAsMap);

        // act
        ParameterizedTypeReference<Map<String, String>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(valueAsString, headers), responseType);

        // assert
        assertThat(responseEntity).satisfies(bookResponseEntity -> {
            assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(bookResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(bookResponseEntity.getBody()).satisfies(errorMessage -> assertThat(errorMessage.get("name")).isEqualTo("'name' can not be empty"));
        });
    }

    @Test
    void test_validBook_missingPublishDate() throws JsonProcessingException {
        // preparation
        Map<String, String> bookasMap = Map.of("id", "12", "authorId", "" + authorEntity.getId(), "name", "Rain");

        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(bookasMap);

        // act
        ParameterizedTypeReference<Map<String, String>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(valueAsString, headers), responseType);

        // assert
        assertThat(responseEntity).satisfies(bookResponseEntity -> {
            assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(bookResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(bookResponseEntity.getBody()).satisfies(errorMessage -> assertThat(errorMessage.get("publishDate")).isEqualTo("'publishDate' can not be empty"));
        });
    }

    @Test
    void test_getBooks() {

        // preparation
        Book book = new Book(12L, authorEntity.getId(), "Mars", LocalDate.of(2012, 4, 15));
        assertThat(restTemplate.postForEntity(uri, new HttpEntity<>(book, headers), Book.class))
            .satisfies(bookResponseEntity -> assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK));

        // act
        ParameterizedTypeReference<List<Book>> responseType = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Book>> responseEntity = restTemplate.exchange(uri.toString() + "?from=0&to=10", HttpMethod.GET, new HttpEntity<>("", headers), responseType);

        //
        assertThat(responseEntity).satisfies(bookResponseEntity -> {
            assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(bookResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(bookResponseEntity.getBody())
                .hasSize(1)
                .satisfies(book1 -> {
                    assertThat(book1.name()).isEqualTo("Mars");
                    assertThat(book1.publishDate()).isEqualTo(LocalDate.of(2012, 4, 15));

                }, atIndex(0));
        });
    }

    @Test
    void test_getBook() {
        // preparation
        Book book = new Book(11L, authorEntity.getId(), "Jupiter", LocalDate.of(2013, 4, 15));
        Book createdBook = restTemplate.postForEntity(uri, new HttpEntity<>(book, headers), Book.class).getBody();
        assertThat(createdBook).isNotNull();

        // act
        ResponseEntity<Book> responseEntity = restTemplate.getForEntity("/api/books/" + createdBook.id(), Book.class);

        //
        assertThat(responseEntity).satisfies(bookResponseEntity -> {
            assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(bookResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(bookResponseEntity.getBody()).satisfies(foundBook -> {
                assertThat(foundBook.name()).isEqualTo("Jupiter");
                assertThat(foundBook.publishDate()).isEqualTo(LocalDate.of(2013, 4, 15));
            });
        });
    }

    @Test
    void test_getBook_notFound() {
        // act
        ResponseEntity<Book> responseEntity = restTemplate.getForEntity("/api/books/" + 12L, Book.class);

        //assert
        assertThat(responseEntity).satisfies(bookResponseEntity -> {
            assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(bookResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
        });
    }

    @Test
    void test_removeBook() {
        // preparation
        Book book = new Book(15L, authorEntity.getId(), "2nd france", LocalDate.of(1999, 4, 15));
        Book createdBook = restTemplate.postForEntity(uri, new HttpEntity<>(book, headers), Book.class).getBody();
        assertThat(createdBook).isNotNull();

        // act
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/api/books/" + createdBook.id(), HttpMethod.DELETE, new HttpEntity<>("", headers), Void.class);
        //assert
        assertThat(responseEntity).satisfies(bookResponseEntity -> assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK));
    }

    @Test
    void test_updateBook() {
        // preparation
        Book book = new Book(5L, authorEntity.getId(), "Stomped", LocalDate.of(1985, 4, 15));
        Book createdBook = restTemplate.postForEntity(uri, new HttpEntity<>(book, headers), Book.class).getBody();
        Book updatedBook = new Book(5L, 12, "Stomped", LocalDate.of(1981, 4, 15));
        assertThat(createdBook).isNotNull();
        //act
        ResponseEntity<Book> responseEntity = restTemplate.exchange("/api/books/" + createdBook.id(), HttpMethod.PUT, new HttpEntity<>(updatedBook), Book.class);
        //assert
        assertThat(responseEntity).satisfies(bookResponseEntity -> {
            assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(bookResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(bookResponseEntity.getBody()).satisfies(responseBook -> assertThat(responseBook.publishDate()).isNotEqualTo(LocalDate.of(1985, 4, 15)));
        });

    }

    @Test
    void test_updateBook2() {
        // Preparation
        Book book = new Book(5L, authorEntity.getId(), "Stomped", LocalDate.of(1985, 4, 15));
        ResponseEntity<Book> createdBookResponse = restTemplate.postForEntity(uri, new HttpEntity<>(book, headers), Book.class);
        Book createdBook = createdBookResponse.getBody();

        // Assert creation
        assertThat(createdBookResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createdBook).isNotNull();

        // Update the book
        Book updatedBook = new Book(createdBook.id(), createdBook.authorId(), createdBook.name(), LocalDate.of(1981, 4, 15));

        // Act
        ResponseEntity<Book> responseEntity = restTemplate.exchange("/api/books/" + createdBook.id(), HttpMethod.PUT, new HttpEntity<>(updatedBook, headers), Book.class);

        // Assert
        assertThat(responseEntity).satisfies(bookResponseEntity -> {
            assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(bookResponseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isEqualTo(List.of("application/json"));
            assertThat(bookResponseEntity.getBody()).satisfies(responseBook -> {
                assertThat(responseBook.publishDate()).isNotEqualTo(LocalDate.of(1985, 4, 15));
                assertThat(responseBook.publishDate()).isEqualTo(LocalDate.of(1981, 4, 15));
            });
        });
    }
}

