package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(@Qualifier("authorRepository") AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRepository.class);
    private Map<String, Author> authorMap = new HashMap<>();
    private String errorMessage = "The author you requested doesn't exist. Please review your parameters!";

    private Author findAuthorAndValidate(String authorId) {
        Author author = authorMap.get(authorId);
        if (author == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }
        return author;
    }

    public Author addAuthor(Author author) {
        String authorId = UUID.randomUUID().toString();
        author.setId(authorId);
        return authorRepository.addAuthor(author);
    }

    public List<Author> getAll() {
        List<Author> authors = authorRepository.getAll();
        if (authors.size() > 0) {
            return authors;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The List you requested appears to be empty. Please add at least one Object before requesting it");
        }
    }

    public Author getAuthor(String authorId) {
        return findAuthorAndValidate(authorId);
    }

    public void deleteAuthor(String authorId) {
        Author author = findAuthorAndValidate(authorId);
        authorRepository.deleteAuthor(author);
    }

    public Author updateAuthor(String authorId, Author authorFromUser) {
        Author author = findAuthorAndValidate(authorId);
        authorFromUser.setId(author.getId());
        return authorRepository.updateAuthor(authorFromUser);
    }
}
