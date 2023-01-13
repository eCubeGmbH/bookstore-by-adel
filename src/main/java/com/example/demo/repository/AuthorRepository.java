package com.example.demo.repository;

import com.example.demo.model.Author;
import com.example.demo.model.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {

}
