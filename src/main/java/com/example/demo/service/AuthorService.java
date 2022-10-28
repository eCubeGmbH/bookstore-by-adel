package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(@Qualifier("authorRepository") AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private final static String errorMessage = "The author you requested doesn't exist. Please review your parameters!";

    private Author findAuthorAndValidate(String authorId) {
        Author author = authorRepository.getAuthor(authorId);
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

    public List<Author> getAll(String authorName) {
        List<Author> authors = authorRepository.getAll();
        if (authors.size() > 0) {
            return StringUtils.hasText(authorName)
                    ? authors.stream()
                        .filter(author -> author.getName().equalsIgnoreCase(authorName.trim()))
                        .collect(Collectors.toList())
                    : authors;
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
