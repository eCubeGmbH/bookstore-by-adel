package com.example.demo.repository;

import com.example.demo.model.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {

    List<AuthorEntity> findByNameIgnoreCase(String name);
}
