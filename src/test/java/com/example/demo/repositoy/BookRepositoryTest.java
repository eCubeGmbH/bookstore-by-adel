package com.example.demo.repositoy;

import com.example.demo.model.entity.BookEntity;
import com.example.demo.repository.BookRepository;
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
import java.util.Random;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Long generateRandomLong() {
        var random = new Random();
        return Math.abs(random.nextLong());
    }

    @Test
    void Test_addBook() {

        var bookId = generateRandomLong();
        var book = new BookEntity(bookId, "authorId", "name", LocalDate.of(2008, 2, 1));

        assertThat(bookRepository.save(book)).satisfies(addedBook -> {
            assertThat(addedBook.getId()).isEqualTo(bookId);
            assertThat(addedBook.getAuthorId()).isEqualTo("authorId");
            assertThat(addedBook.getName()).isEqualTo("name");
            assertThat(addedBook.getPublishDate()).isEqualTo(LocalDate.of(2008, 2, 1));
        });
    }

    @Test
    void test_findByNameIgnoreCase() {
        var bookId = 16L;
        var bookId1 = 14L;
        var bookId2 = 12L;
        var book = new BookEntity(bookId, "authorId", "Adel", LocalDate.of(1997, 1, 11));
        var book1 = new BookEntity(bookId1, "authorId", "adel", LocalDate.of(1997, 1, 11));
        var book2 = new BookEntity(bookId2, "authorId", "adEL", LocalDate.of(1997, 1, 11));
        bookRepository.save(book);
        bookRepository.save(book1);
        bookRepository.save(book2);


        var sortOrder = Sort.by("name").ascending()
            .and(Sort.by("id").ascending());

        var pageRequest = PageRequest.of(0, 5).withSort(sortOrder);
        assertThat(bookRepository.findByNameIgnoreCase("adel", pageRequest))
            .hasSize(3)
            .isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name", "nAme", "namE", "nAMe"})
    void test_findByNameIgnoreCase2(String name) {
        var bookId = generateRandomLong();
        var book = new BookEntity(bookId, "authorId", "name", LocalDate.of(1977, 1, 22));
        bookRepository.save(book);

        assertThat(bookRepository.findByNameIgnoreCase(name, PageRequest.of(0, 2)))
            .isNotEmpty()
            .hasSize(1);
    }

    @Test
    void Test_getBooks() {
        var bookId = generateRandomLong();
        var book = new BookEntity(bookId, "authorId", "name", LocalDate.of(1997, 1, 1));
        bookRepository.save(book);

        assertThat(bookRepository.findAll())
            .isNotEmpty()
            .hasSize(1)
            .satisfies(book1 -> {
                assertThat(book1.getId()).isEqualTo(bookId);
                assertThat(book1.getAuthorId()).isEqualTo("authorId");
                assertThat(book1.getName()).isEqualTo("name");
                assertThat(book1.getPublishDate()).isEqualTo(LocalDate.of(1997, 1, 1));

            }, Index.atIndex(0));
    }

    @Test
    void test_getBook() {
        var bookId = generateRandomLong();
        var book = new BookEntity(bookId, "authorId", "name", LocalDate.of(1997, 1, 1));
        bookRepository.save(book);

        assertThat(bookRepository.findById(bookId))
            .isPresent()
            .get().satisfies(foundBook -> {
                assertThat(foundBook.getId()).isEqualTo(bookId);
                assertThat(foundBook.getAuthorId()).isEqualTo("authorId");
                assertThat(foundBook.getName()).isEqualTo("name");
                assertThat(foundBook.getPublishDate()).isEqualTo(LocalDate.of(1997, 1, 1));
            });
    }

    @Test
    void test_getBookByAuthorId() {
        var bookId = 15L;
        var bookId1 = 10L;
        var authorId = "authorId";
        var book = new BookEntity(bookId, authorId, "name", LocalDate.of(2004, 2, 1));
        var book1 = new BookEntity(bookId1, authorId, "name", LocalDate.of(2004, 2, 1));
        bookRepository.save(book);
        bookRepository.save(book1);

        assertThat(bookRepository.findByAuthorId(authorId))
            .hasSize(2)
            .containsExactlyInAnyOrder(book, book1);
    }

    @Test
    void Test_removeBook() {
        var bookId = generateRandomLong();
        BookEntity book = new BookEntity(bookId, "authorId", "name", LocalDate.of(1997, 1, 1));
        bookRepository.save(book);
        bookRepository.delete(book);
    }

    @Test
    void Test_updateBook() {
        var bookId = generateRandomLong();
        BookEntity book = new BookEntity(bookId, "authorId", "name", LocalDate.of(1997, 1, 1));
        bookRepository.save(book);

        book.setPublishDate(LocalDate.of(2003, 12, 24));
        book.setName("Physics");

        assertThat(bookRepository.save(book))
            .satisfies(updatedBook -> {
                assertThat(updatedBook.getId()).isEqualTo(bookId);
                assertThat(updatedBook.getAuthorId()).isEqualTo("authorId");
                assertThat(updatedBook.getName()).isEqualTo("Physics");
                assertThat(updatedBook.getPublishDate()).isEqualTo(LocalDate.of(2003, 12, 24));
            });
    }
}

