package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record Author(
    Long id,
    @NotBlank(message = "'name' can not be empty") String name,
    @NotNull(message = "'country' can not be empty") String country,
    @NotNull(message = "'birthDate' can not be empty") LocalDate birthDate
) {
}
