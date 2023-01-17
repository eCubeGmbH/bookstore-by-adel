package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(@Qualifier("authorRepository") AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private final static String errorMessage = "The author you requested doesn't exist. Please review your parameters!";

    public Author addAuthor(Author author) {
        String authorId = UUID.randomUUID().toString();
        AuthorEntity authorEntity = new AuthorEntity(authorId, author.getName(), author.getCountry(), author.getBirthDate());
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        return toAuthor(savedAuthorEntity);
    }

    public List<Author> getAll(String authorName, int from, int to) {
        List<AuthorEntity> authorEntities = authorRepository.findAll();
        List<Author> foundAuthors = new ArrayList<>();

        // filter
        if (authorName == null || authorName.isBlank()) {
            for (AuthorEntity authorEntity : authorEntities) {
                foundAuthors.add(toAuthor(authorEntity));
            }
        } else {
            for (AuthorEntity authorEntity : authorEntities) {
                if (authorEntity.getName().equalsIgnoreCase(authorName.trim())) {
                    foundAuthors.add(toAuthor(authorEntity));
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
        if (from > foundAuthors.size()) { //from > Anzahl authoren => leere Liste
            return List.of();
        }
        return foundAuthors.subList(from, Math.min(to, foundAuthors.size()));
    }


    public Author getAuthor(String authorId) {
        return toAuthor(findAuthorAndValidate(authorId));
    }

    public void deleteAuthor(String authorId) {
        AuthorEntity foundAuthor = findAuthorAndValidate(authorId);
        authorRepository.delete(foundAuthor);
    }

    public Author updateAuthor(String authorId, Author authorFromUser) {
        AuthorEntity foundAuthor = findAuthorAndValidate(authorId);
        foundAuthor.setName(authorFromUser.getName());
        foundAuthor.setCountry(authorFromUser.getCountry());
        foundAuthor.setBirthDate(authorFromUser.getBirthDate());
        AuthorEntity savedAuthorEntity = authorRepository.save(foundAuthor);
        return toAuthor(savedAuthorEntity);
    }

    private AuthorEntity findAuthorAndValidate(String authorId) {
        Optional<AuthorEntity> maybeAuthorEntity = authorRepository.findById(authorId);
        if (maybeAuthorEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        } else {
            return maybeAuthorEntity.get();
        }
    }

    private Author toAuthor(AuthorEntity authorEntity) {
        return new Author(authorEntity.getId(), authorEntity.getName(), authorEntity.getCountry(), authorEntity.getBirthDate());
    }
}
