package com.example.demo.repository;

import com.example.demo.model.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {

    List<AuthorEntity> findByNameIgnoreCase(String name);

    @Query("SELECT author FROM AuthorEntity author WHERE LOWER(author.name) = LOWER(:name)")
    List<AuthorEntity> findByName(@Param("name") String name);

}
