package com.example.demo.model.entity;

import com.example.demo.model.Book;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.Month;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BookEntityTest {
    @Test
    void test_new_Book() {
        Book book = new Book(12L, "5", "Animals in the Ocean", LocalDate.of(1958, 4, 11));
        assertEquals(12L, book.id());
        assertEquals("5", book.authorId());
        assertEquals("Animals in the Ocean", book.name());
        assertEquals(LocalDate.of(1958, Month.APRIL, 11), book.publishDate());
    }
}

