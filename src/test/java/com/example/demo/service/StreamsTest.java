package com.example.demo.service;

import com.example.demo.model.entity.AuthorEntity;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamsTest {

    private final AuthorEntity authorEntity1 = new AuthorEntity("John", "USA", LocalDate.of(1997, 1, 2), new ArrayList<>());
    private final AuthorEntity AuthorEntity2 = new AuthorEntity("Müller", "USA", LocalDate.of(1997, 1, 2), new ArrayList<>());
    private final AuthorEntity AuthorEntity3 = new AuthorEntity("müller", "USA", LocalDate.of(1997, 1, 2), new ArrayList<>());
    private final AuthorEntity AuthorEntity4 = new AuthorEntity("Meier", "USA", LocalDate.of(1997, 1, 2), new ArrayList<>());
    private final AuthorEntity AuthorEntity5 = new AuthorEntity("Rein", "DÄN", LocalDate.of(1920, 1, 2), new ArrayList<>());
    List<AuthorEntity> authors = List.of(authorEntity1, AuthorEntity2, AuthorEntity3, AuthorEntity4, AuthorEntity5);

    @Test
    void testStreamFilter() {
        List<AuthorEntity> requiredAuthors = authors.stream()
            .filter(author -> author.getCountry().equals("USA"))
            .toList();
        assertThat(requiredAuthors)
            .hasSize(4)
            .containsExactly(authorEntity1, AuthorEntity2, AuthorEntity3, AuthorEntity4);
    }

    @Test
    void testStreamMapping() {
        List<Long> requiredAuthors = authors.stream()
            .map(AuthorEntity::getId)
            .toList();
        assertEquals(5, requiredAuthors.size());
    }

    @Test
    void testStreamForEach() {
        List<AuthorEntity> requiredAuthors = new ArrayList<>(authors);
        assertThat(requiredAuthors)
            .hasSize(5);
    }

    @Test
    void testForEach() {
        List<AuthorEntity> requiredAuthors = new ArrayList<>(authors);
        assertThat(requiredAuthors)
            .hasSize(5);

    }


    @Test
    void testStreamListToMap2() {
        Map<String, List<AuthorEntity>> authorEntityMap = authors.stream()
            .collect(Collectors.groupingBy(AuthorEntity::getName));
        assertThat(authorEntityMap)
            .isNotEmpty()
            .hasSize(5);
    }

}
