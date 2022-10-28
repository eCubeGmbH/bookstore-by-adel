package com.example.demo.repositoy;

import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.AuthorRepositoryListImpl;
import com.example.demo.repository.AuthorRepositoryMapImpl;
import com.example.demo.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthorRepositoryTest {

    @Test
    void addAuthorShouldAddAuthorToRepositoryListImpl() {
        //setup, Vorbereitung (sut) system under test
        AuthorRepository sut = new AuthorRepositoryListImpl();
        Author author1 = new Author();
        author1.setName("Luc");

        //when, Ausführen
        Author authorFromAddAuthor = sut.addAuthor(author1);
        Author actual = sut.getAuthor(authorFromAddAuthor.getId());

        //then, Verifizieren
        assertNotNull(actual, "response should not be null");
        assertEquals(authorFromAddAuthor.getId(), actual.getId(), "ids should match");
        assertEquals( "Luc", actual.getName(), "names should match");
    }

    @Test
    void addAuthorShouldAddAuthorToRepositoryMapImpl() {
        //setup, Vorbereitung (sut) system under test
        AuthorRepository sut = new AuthorRepositoryMapImpl();
        Author author1 = new Author();
        author1.setName("Luc");

        //when, Ausführen
        Author authorFromAddAuthor = sut.addAuthor(author1);
        Author actual = sut.getAuthor(authorFromAddAuthor.getId());

        //then, Verifizieren
        assertNotNull(actual, "response should not be null");
        assertEquals(authorFromAddAuthor.getId(), actual.getId(), "ids should match");
        assertEquals( "Luc", actual.getName(), "names should match");
    }

    @Test
    void deleteAuthorShouldRemoveFromRepositoryListImpl() {
        //setup, Vorbereitung (sut) system under test
        AuthorRepository sut = new AuthorRepositoryListImpl();
        Author author1 = new Author();
        author1.setName("Luc");

        //when, Ausführen
        Author authorFromAddAuthor = sut.addAuthor(author1);
        sut.deleteAuthor(authorFromAddAuthor.getId());

        //then, Verifizieren
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            sut.deleteAuthor(authorFromAddAuthor.getId());
        });
        String expectedMessage = "The author you requested doesn't exist. Please review your parameters!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteAuthorShouldRemoveFromRepositoryMapImpl() {
        //setup, Vorbereitung (sut) system under test
        AuthorRepository sut = new AuthorRepositoryMapImpl();
        Author author1 = new Author();
        author1.setName("Luc");

        //when, Ausführen
        Author authorFromAddAuthor = sut.addAuthor(author1);
        sut.deleteAuthor(authorFromAddAuthor.getId());

        //then, Verifizieren
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            sut.deleteAuthor(authorFromAddAuthor.getId());
        });
        String expectedMessage = "The author you requested doesn't exist. Please review your parameters!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateAuthorShouldUpdateAuthorInRepositoryListImpl() {
        //setup, Vorbereitung (sut) system under test
        AuthorRepository sut = new AuthorRepositoryListImpl();
        Author author1 = new Author();
        author1.setName("Luc");

        //when, Ausführen
        Author authorFromAddAuthor = sut.addAuthor(author1);
        Author actual = sut.getAuthor(authorFromAddAuthor.getId());
        actual.setId(authorFromAddAuthor.getId());
        actual.setName("David");

        //then, Verifizieren
        assertNotNull(actual, "response should not be null");
        assertEquals(authorFromAddAuthor.getId(), actual.getId(), "ids should match");
        assertNotEquals( "Luc", actual.getName(), "names should not match");
    }

    @Test
    void updateAuthorShouldUpdateAuthorInRepositoryMapImpl() {
        //setup, Vorbereitung (sut) system under test
        AuthorRepository sut = new AuthorRepositoryMapImpl();
        Author author1 = new Author();
        author1.setName("Luc");

        //when, Ausführen
        Author authorFromAddAuthor = sut.addAuthor(author1);
        Author actual = sut.getAuthor(authorFromAddAuthor.getId());
        actual.setId(authorFromAddAuthor.getId());
        actual.setName("David");

        //then, Verifizieren
        assertNotNull(actual, "response should not be null");
        assertEquals(authorFromAddAuthor.getId(), actual.getId(), "ids should match");
        assertNotEquals( "Luc", actual.getName(), "names should not match");
    }

    @Test
    void getAllAuthorsShouldUpdateAuthorInRepositoryListImpl() {
        //setup, Vorbereitung (sut) system under test
        AuthorRepository sut = new AuthorRepositoryListImpl();
        Author author1 = new Author();
        author1.setName("Dee");
        Author author2 = new Author();
        author2.setName("Dux");
        Author author3 = new Author();
        author3.setName("Doo");
        Author author4 = new Author();
        author4.setName("Dum");
        Author author5 = new Author();
        author5.setName("Dam");

        //when, Ausführen
        sut.addAuthor(author1);
        sut.addAuthor(author2);
        sut.addAuthor(author3);
        sut.addAuthor(author4);
        sut.addAuthor(author5);

        List<Author> list = sut.getAll();

        //then, Verifizieren
        assertNotNull(list, "response should not be null");
        assertEquals(5, list.size(), "list size should match");
    }

    @Test
    void getAllAuthorsShouldUpdateAuthorInRepositoryMapImpl() {
        //setup, Vorbereitung (sut) system under test
        AuthorRepository sut = new AuthorRepositoryMapImpl();
        Author author1 = new Author();
        author1.setName("Dee");
        Author author2 = new Author();
        author2.setName("Dux");
        Author author3 = new Author();
        author3.setName("Doo");
        Author author4 = new Author();
        author4.setName("Dum");
        Author author5 = new Author();
        author5.setName("Dam");

        //when, Ausführen
        sut.addAuthor(author1);
        sut.addAuthor(author2);
        sut.addAuthor(author3);
        sut.addAuthor(author4);
        sut.addAuthor(author5);

        List<Author> list = sut.getAll();

        //then, Verifizieren
        assertNotNull(list, "response should not be null");
        assertEquals(5, list.size(), "list size should match");
    }
}


