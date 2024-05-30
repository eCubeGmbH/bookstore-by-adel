package com.example.demo.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthorEntity extends BasicEntity {

    public AuthorEntity(String name, String country, LocalDate birthDate){
        this.name = name;
        this.country = country;
        this.birthDate = birthDate;
        this.booksList = new ArrayList<>();
    }

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "authorReference")
    @OrderBy("id")
    private List<BookEntity> booksList;
}
