package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "book")
@EqualsAndHashCode //lombok auto-generate
@Getter//lombok auto-generate
@Setter//lombok auto-generate
@NoArgsConstructor//lombok auto-generate
@AllArgsConstructor//lombok auto-generate
public class BookEntity {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "authorId")
    private String authorId;

    @Column(name = "name")
    private String name;

    @Column(name = "publishDate")
    private LocalDate publishDate;
}


