package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.model.entity.AuthorEntity;
import com.example.demo.model.enums.SortField;
import com.example.demo.model.enums.SortOrder;
import com.example.demo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    final static String errorMessage = "The author you requested doesn't exist. Please review your parameters!";

    public Author addAuthor(Author author) {
        String authorId = UUID.randomUUID().toString();
        AuthorEntity authorEntity = new AuthorEntity(authorId, author.name().trim(), author.country(), author.birthDate(), new ArrayList<>());
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        return toAuthor(savedAuthorEntity);
    }


    public List<Author> getAll(int pageNumber, int pageSize, SortField sortField, SortOrder sortOrder, Optional<String> maybeAuthorName) {
        // Sorting
        Sort sort = Sort.by(Sort.Direction.valueOf(sortOrder.name()), sortField.getFieldName());

        // Pagination + Sorting
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        List<AuthorEntity> authorEntities = maybeAuthorName.isPresent()
            ? authorRepository.findByName(maybeAuthorName.get().trim(), pageRequest)
            : authorRepository.findAll(pageRequest).getContent();

        return authorEntities.stream()
            .map(authorEntity -> toAuthor(authorEntity))
            .toList();
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
        foundAuthor.setName(authorFromUser.name());
        foundAuthor.setCountry(authorFromUser.country());
        foundAuthor.setBirthDate(authorFromUser.birthDate());
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
