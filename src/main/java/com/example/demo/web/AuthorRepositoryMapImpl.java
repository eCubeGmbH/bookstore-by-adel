package com.example.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

public class AuthorRepositoryMapImpl implements AuthorRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRepositoryMapImpl.class);
    private Map<String, Authors> authorMap = new HashMap<>();

    private String errorMessage = "The author you requested doesn't exist. Please review your parameters!";

    private Object errorChecking(String authorId) {
        if(authorMap.get(authorId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }
        return new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
    }

    public Authors postAuthors(Authors author) {
        String authorId = UUID.randomUUID().toString();
        author.setId(authorId);
        authorMap.put(authorId, author);
        return author;
    }

    public Authors getAuthors(String authorId) {
        LOGGER.info("coming from map");
        errorChecking(authorId);
        Authors getAuthor = authorMap.get(authorId);
        return getAuthor;
    }

    public void deleteAuthors(String authorId) {
        errorChecking(authorId);
        authorMap.remove(authorId);
    }

    public Authors putAuthors(String authorId, Authors authorFromUser) {
        errorChecking(authorId);
        authorFromUser.setId(authorId);
        authorMap.replace(authorId, authorFromUser);
        return authorFromUser;
    }
}
