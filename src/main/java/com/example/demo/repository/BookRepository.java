package com.example.demo.repository;

import com.example.demo.model.Book;
import com.example.demo.model.entity.BookEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    List<BookEntity> findByName(String trim, PageRequest pageRequest);

    List<Book> findByAuthorId(String authorId);

    List<BookEntity> findByNameIgnoreCase(String name);


}
