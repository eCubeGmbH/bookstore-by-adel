package com.example.demo.model;

import com.example.demo.model.enums.SortOrder;
import lombok.Getter;
import java.util.List;

public record BooksEnvelopDto(
    int pageNumber,
    int pageSize,
    long booksCount,
    SortField sortField,
    SortOrder sortOrder,
    String bookName,
    List<Book> bookList
) {
    @Getter
    public enum SortField {

        ID("id"),
        AUTHOR_ID("authorReference.id"),
        NAME("name"),
        PUBLISH_DATE("publishDate");

        private final String fieldName;

        SortField(String fieldName) {
            this.fieldName = fieldName;
        }
    }
}
