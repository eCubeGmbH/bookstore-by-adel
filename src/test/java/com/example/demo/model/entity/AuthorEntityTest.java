package com.example.demo.model.entity;

import com.example.demo.model.Author;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.Month;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorEntityTest {
    @Test
    void test_new_Author() {
        Author author = new Author("ABC123", "steve", "france", LocalDate.of(1958, 4, 11));
        assertEquals("ABC123", author.id());
        assertEquals("steve", author.name());
        assertEquals("france", author.country());
        assertEquals(LocalDate.of(1958, Month.APRIL, 11), author.birthDate());
    }
}
