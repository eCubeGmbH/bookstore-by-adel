package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record Book(
    Long id,

    String authorId,

    @NotBlank(message = "'name' can not be empty") String name,
    @NotNull(message = "'publishDate' can not be empty") LocalDate publishDate
) {
}