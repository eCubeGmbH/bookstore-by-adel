package com.example.demo.web;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {
    @Test
    void test_new_Author() {
        Author author = new Author("ABC123", "steve", "farnce", LocalDate.of(1958, 4, 11));
        assertEquals("ABC123", author.getId());
        assertEquals("steve", author.getName());
        assertEquals("farnce", author.getCountry());
        assertEquals(LocalDate.of(1958, Month.APRIL, 11), author.getBirthDate());
    }

}