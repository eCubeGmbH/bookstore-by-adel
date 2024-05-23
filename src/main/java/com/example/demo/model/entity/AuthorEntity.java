package com.example.demo.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
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

    @Setter
    @Column(name = "name")
    private String name;

    @Setter
    @Column(name = "country")
    private String country;

    @Setter
    @Column(name = "birth_date")
    private LocalDate birthDate;


    @Setter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "authorId")
    @OrderBy("id")
    private List<BookEntity> BooksList;


}
