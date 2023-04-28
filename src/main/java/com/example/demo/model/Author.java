package com.example.demo.model;

import java.time.LocalDate;

public record Author(String id, String name, String country, LocalDate birthDate) {
}
