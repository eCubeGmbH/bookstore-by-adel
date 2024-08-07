package com.example.demo.model;

import com.example.demo.model.enums.SortOrder;
import lombok.Getter;
import java.util.List;


public record AuthorsEnvelopDto(
    int pageNumber,
    int pageSize,
    long authorsCount,
    SortField sortField,
    SortOrder sortOrder,
    String authorName,
    List<Author> authors
) {
    @Getter
    public enum SortField {

        ID("id"),
        NAME("name"),
        COUNTRY("country"),
        BIRTHDATE("birthDate");

        private final String fieldName;

        SortField(String fieldName) {
            this.fieldName = fieldName;
        }
    }
}