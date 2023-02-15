package com.example.demo.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorTest {
    @Test
    void test_new_Author() {
        Author author = new Author("ABC123", "steve", "farnce", LocalDate.of(1958, 4, 11));
        assertEquals("ABC123", author.id());
        assertEquals("steve", author.name());
        assertEquals("farnce", author.country());
        assertEquals(LocalDate.of(1958, Month.APRIL, 11), author.birthDate());
    }

}