package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "author")
public class AuthorEntity {

    public AuthorEntity() {

    }

    public AuthorEntity(String id, String name, String country, LocalDate birthDate, ArrayList<BookEntity> booksList) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.birthDate = birthDate;
        this.BooksList = booksList;
    }

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "birth_date")
    private LocalDate birthDate;


    @OneToMany
    @OrderBy("id")
    private List<BookEntity> BooksList;


    public List<BookEntity> getBooksList() {
        return BooksList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setBooksList(List<BookEntity> booksList) {
        BooksList = booksList;
    }

}
