package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "book")
public class BookEntity {

    public BookEntity() {
    }

    public BookEntity(long id, String authorId, String name, LocalDate publishDate) {
        this.id = id;
        this.authorId = authorId;
        this.name = name;
        this.publishDate = publishDate;
    }

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "authorId")
    private String authorId;

    @Column(name = "name")
    private String name;

    @Column(name = "publishDate")
    private LocalDate publishDate;

    public long getId() {
        return id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

}


