package com.example.demo.web;

import com.example.demo.model.Author;
import com.example.demo.model.enums.SortField;
import com.example.demo.model.enums.SortOrder;
import com.example.demo.service.AuthorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping(value = {"/api/authors"})
class AuthorController implements IAuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public Author addAuthor(@Valid @RequestBody Author author) {
        return authorService.addAuthor(author);
    }

    @Valid
    @Override
    @GetMapping(produces = {"application/json"})
    public List<Author> getAllAuthors(
        @RequestParam(value = "pageNumber") int pageNumber,
        @RequestParam(value = "pageSize") int pageSize,
        @RequestParam(value = "sortField", defaultValue = "NAME") SortField sortField,
        @RequestParam(value = "sortOrder", defaultValue = "ASC") SortOrder sortOrder,
        @RequestParam(value = "maybeAuthorName", required = false) Optional<String> maybeAuthorName
    ) {

        return authorService.getAll(pageNumber, pageSize, sortField, sortOrder, maybeAuthorName);
    }

    @Override
    @GetMapping(value = {"/{authorId}"}, produces = {"application/json"})

    public Author getAuthor(@PathVariable String authorId) {
        return authorService.getAuthor(authorId);
    }

    @Override
    @DeleteMapping(value = {"/{authorId}"}, consumes = {"application/json"})

    public void removeAuthor(@PathVariable String authorId) {
        authorService.deleteAuthor(authorId);
    }

    @Override
    @PutMapping(value = {"/{authorId}"}, consumes = {"application/json"}, produces = {"application/json"})

    public Author updateAuthor(@PathVariable String authorId, @Valid @RequestBody Author authorFromUser) {
        return authorService.updateAuthor(authorId, authorFromUser);
    }
}
