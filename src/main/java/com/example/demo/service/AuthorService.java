package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.AuthorRepositoryMapImpl;
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


@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(@Qualifier("authorRepositoryMapImpl") AuthorRepository authorRepository) {

        this.authorRepository = authorRepository;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRepositoryMapImpl.class);
    private Map<String, Author> authorMap = new HashMap<>();
    private String errorMessage = "The author you requested doesn't exist. Please review your parameters!";

    private Object errorChecking(String authorId) {
        if (authorMap.get(authorId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }
        return new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
    }

    public Author addAuthor(Author author) {
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
        LOGGER.info("coming from map");
        errorChecking(authorId);
        return getAuthor(authorId);
    }

    public void deleteAuthor(String authorId) {
        errorChecking(authorId);
        authorRepository.deleteAuthor(authorId);
    }

    public Author updateAuthor(String authorId, Author authorFromUser) {
        errorChecking(authorId);
        return authorRepository.updateAuthor(authorId, authorFromUser);
    }
}


