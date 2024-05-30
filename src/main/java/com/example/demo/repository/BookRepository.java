package com.example.demo.repository;

import com.example.demo.model.entity.BookEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findByNameIgnoreCase(String name, PageRequest pageRequest);
}
