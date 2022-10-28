package com.example.demo.repository;

import com.example.demo.model.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Repository
public class AuthorRepositoryMapImpl implements AuthorRepository {

    private Map<String, Author> authorMap = new HashMap<>();


    public Author addAuthor(Author author) {
        String authorId = UUID.randomUUID().toString();
        author.setId(authorId);
        authorMap.put(authorId, author);
        return author;
    }

    public List<Author> getAll() {
        return (List<Author>) authorMap.values();

    }

    public Author getAuthor(String authorId) {
        Author getAuthor = authorMap.get(authorId);
        return getAuthor;
    }

    public void deleteAuthor(String authorId) {
        authorMap.remove(authorId);
    }

    public Author updateAuthor(String authorId, Author author) {
        author.setId(authorId);
        authorMap.replace(authorId, author);
        return author;
    }
}
