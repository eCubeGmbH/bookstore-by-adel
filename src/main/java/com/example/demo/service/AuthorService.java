package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.model.AuthorsEnvelopDto;
import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.model.enums.SortField;
import com.example.demo.model.enums.SortOrder;
import com.example.demo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    final static String errorMessage = "The author you requested doesn't exist. Please review your parameters!";

    public Author addAuthor(Author author) {
        AuthorEntity authorEntity = new AuthorEntity(author.name().trim(), author.country(), author.birthDate(), new ArrayList<>());
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        return toAuthor(savedAuthorEntity);
    }

    public AuthorsEnvelopDto getAll(int pageNumber, int pageSize, SortField sortField, SortOrder sortOrder, Optional<String> maybeAuthorName) {
        // Sorting
        Sort sort = Sort.by(sortOrder == SortOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, sortField.getFieldName());

        // Pagination + Sorting
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<AuthorEntity> authorsCount = maybeAuthorName
            .map(name -> authorRepository.findByName(name.trim(), pageRequest))
            .orElseGet(() -> authorRepository.findAll(pageRequest));

        List<Author> authors = authorsCount.getContent().stream()
            .map(this::toAuthor)
            .toList();

        return new AuthorsEnvelopDto(
            pageNumber,
            pageSize,
            authorsCount.getTotalElements(),
            sortField,
            sortOrder,
            maybeAuthorName.orElse(null),
            authors
        );
    }

    public Author getAuthor(long authorId) {
        return toAuthor(findAuthorAndValidate(authorId));
    }

    public void deleteAuthor(long authorId) {
        AuthorEntity foundAuthor = findAuthorAndValidate(authorId);
        authorRepository.delete(foundAuthor);
    }

    public Author updateAuthor(long authorId, Author authorFromUser) {
        AuthorEntity foundAuthor = findAuthorAndValidate(authorId);
        foundAuthor.setName(authorFromUser.name());
        foundAuthor.setCountry(authorFromUser.country());
        foundAuthor.setBirthDate(authorFromUser.birthDate());
        AuthorEntity savedAuthorEntity = authorRepository.save(foundAuthor);
        return toAuthor(savedAuthorEntity);
    }

    private AuthorEntity findAuthorAndValidate(long authorId) {
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
