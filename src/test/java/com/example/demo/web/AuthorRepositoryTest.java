package com.example.demo.web;

import org.junit.jupiter.api.Test;

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
}
