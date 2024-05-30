package com.example.demo.repository;

import com.example.demo.model.entity.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    @Query("""
        SELECT author
          FROM AuthorEntity author
         WHERE LOWER(author.name) = LOWER(:name)
        """)
    Page<AuthorEntity> findByName(String name, Pageable pageable);
}
