package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public List<Author> getAll(String authorName, int from, int to) {
        List<Author> authors = authorRepository.getAll();
        List<Author> foundAuthors = new ArrayList<>();

        // filter
        if (authorName == null || authorName.isBlank()) {
            foundAuthors.addAll(authors);
        } else {
            for (Author author : authors) {
                if (author.getName().equalsIgnoreCase(authorName.trim())) {
                    foundAuthors.add(author);
                }
            }
        }

        // sorting
        foundAuthors.sort((author1, author2) -> {
            int compareTo = author1.getName().compareToIgnoreCase(author2.getName());
            if (compareTo == 0) {
                return author1.getId().compareTo(author2.getId());
            } else {
                return compareTo;
            }
        });

        // pagination
        if (from > foundAuthors.size()) {
            return List.of();
        }
        return foundAuthors.subList(from, Math.min(to, foundAuthors.size()));
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
