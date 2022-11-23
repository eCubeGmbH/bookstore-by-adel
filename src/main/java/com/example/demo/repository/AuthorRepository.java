package com.example.demo.repository;

import com.example.demo.model.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AuthorRepository {

    private final Map<String, Author> authorMap = new HashMap<>();

    public Author addAuthor(Author author) {
        authorMap.put(author.getId(), author);
        return author;
    }

    public List<Author> getAll() {
        return new ArrayList<>(authorMap.values());
    }

    public Author getAuthor(String authorId) {
        return authorMap.get(authorId);
    }

    public void deleteAuthor(Author author) {
        authorMap.remove(author.getId());
    }

    public Author updateAuthor(Author author) {
        authorMap.replace(author.getId(), author);
        return author;
    }
}
