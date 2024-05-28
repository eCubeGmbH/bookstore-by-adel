package com.example.demo.repositoy;

import com.example.demo.model.entity.AuthorEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestAuthorRepository extends JpaRepository<AuthorEntity, String> {

    List<AuthorEntity> findByNameIgnoreCase(String name);

    @Query("""
        SELECT author
          FROM AuthorEntity author
         WHERE LOWER(author.name) = LOWER(:name)
        """)
    List<AuthorEntity> findByName(@Param("name") String name);

    @Query(value = """
        SELECT *
          FROM author
         WHERE LOWER(name) = LOWER(:name)
        """, nativeQuery = true)
    List<AuthorEntity> findByNameNative(@Param("name") String name);

    List<AuthorEntity> findByName(String name, Sort sort);

    List<AuthorEntity> findByName(String name, org.springframework.data.domain.Pageable pageable);
}