package com.example.demo.repositoy;

import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.model.entity.BookEntity;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.BeforeEach;
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
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private AuthorEntity authorEntity;

    @BeforeEach
    public void setUp() {
        AuthorEntity author = new AuthorEntity("foo", "usa", LocalDate.of(2000, 10, 24), new ArrayList<>());
        authorEntity = authorRepository.save(author);
    }

    @Test
    void Test_addBook() {
        var book = new BookEntity(authorEntity, "name", LocalDate.of(2008, 2, 1));

        assertThat(bookRepository.save(book)).satisfies(addedBook -> {
            assertThat(addedBook.getName()).isEqualTo("name");
            assertThat(addedBook.getPublishDate()).isEqualTo(LocalDate.of(2008, 2, 1));
        });
    }

    @Test
    void test_findByNameIgnoreCase() {
        var book = new BookEntity(authorEntity, "Adel", LocalDate.of(1997, 1, 11));
        var book1 = new BookEntity(authorEntity, "adel", LocalDate.of(1997, 1, 11));
        var book2 = new BookEntity(authorEntity, "adEL", LocalDate.of(1997, 1, 11));
        bookRepository.save(book);
        bookRepository.save(book1);
        bookRepository.save(book2);


        var sortOrder = Sort.by("name").ascending()
            .and(Sort.by("id").ascending());

        var pageRequest = PageRequest.of(0, 5).withSort(sortOrder);
        assertThat(bookRepository.findByNameIgnoreCase("adel", pageRequest))
            .hasSize(3);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name", "nAme", "namE", "nAMe"})
    void test_findByNameIgnoreCase2(String name) {


        var book = new BookEntity(authorEntity, "name", LocalDate.of(1977, 1, 22));
        bookRepository.save(book);

        assertThat(bookRepository.findByNameIgnoreCase(name, PageRequest.of(0, 2)))
            .hasSize(1);
    }

    @Test
    void test_FindAllSortedByAuthorId() {
        var bookN = new BookEntity(authorEntity, "Neugierig", LocalDate.of(1955,1,12));
        var bookF = new BookEntity(authorEntity, "Funny", LocalDate.of(1955,1,12));
        var bookG = new BookEntity(authorEntity, "Glad", LocalDate.of(1955,1,12));

        bookRepository.save(bookN);
        bookRepository.save(bookF);
        bookRepository.save(bookG);

        List<BookEntity> books = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "authorReference.id"));

        assertThat(books).hasSize(3);
    }

    @Test
    void Test_getBooks() {
        var book = new BookEntity(authorEntity, "name", LocalDate.of(1997, 1, 1));
        bookRepository.save(book);

        assertThat(bookRepository.findAll())
            .isNotEmpty()
            .hasSize(1)
            .satisfies(book1 -> {
                assertThat(book1.getAuthorReference()).isEqualTo(authorEntity);
                assertThat(book1.getName()).isEqualTo("name");
                assertThat(book1.getPublishDate()).isEqualTo(LocalDate.of(1997, 1, 1));

            }, Index.atIndex(0));
    }

    @Test
    void Test_removeBook() {
        BookEntity book = new BookEntity(authorEntity, "name", LocalDate.of(1997, 1, 1));
        bookRepository.save(book);
        bookRepository.delete(book);
    }

    @Test
    void Test_updateBook() {

        BookEntity book = new BookEntity(authorEntity, "name", LocalDate.of(1997, 1, 1));
        bookRepository.save(book);

        book.setPublishDate(LocalDate.of(2003, 12, 24));
        book.setName("Physics");

        assertThat(bookRepository.save(book))
            .satisfies(updatedBook -> {
                assertThat(updatedBook.getAuthorReference()).isEqualTo(authorEntity);
                assertThat(updatedBook.getName()).isEqualTo("Physics");
                assertThat(updatedBook.getPublishDate()).isEqualTo(LocalDate.of(2003, 12, 24));
            });
    }
}

