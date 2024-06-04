package com.example.demo.model;

import com.example.demo.model.enums.SortField;
import com.example.demo.model.enums.SortOrder;
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
}