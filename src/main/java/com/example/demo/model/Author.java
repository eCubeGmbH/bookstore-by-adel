package com.example.demo.model;

import java.time.LocalDate;
import java.util.Objects;

public class Author {
    private String id;
    private String name;
    private String country;
    private LocalDate birthDate;

    public Author() {
    }

    public Author(String id, String name, String country, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.birthDate = birthDate;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Author author = (Author) o;
        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Author{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", country='" + country + '\'' +
            ", birthDate=" + birthDate +
            '}';
    }
}
