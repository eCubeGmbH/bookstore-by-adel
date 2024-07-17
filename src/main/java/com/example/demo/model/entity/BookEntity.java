package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "book")
@Getter //lombok auto-generate
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BookEntity extends BasicEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private AuthorEntity authorReference;

    @Column(name = "name")
    private String name;

    @Column(name = "publishDate")
    private LocalDate publishDate;
}


