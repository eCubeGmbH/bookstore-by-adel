package com.example.demo.repository;

import com.example.demo.model.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Page<BookEntity> findByNameIgnoreCase(String name, Pageable pageable);
}
