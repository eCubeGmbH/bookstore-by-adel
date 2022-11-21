package com.example.demo.web;

import com.example.demo.model.Author;
import com.example.demo.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseBody
@RequestMapping(value = {"/api/authors"})
class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(consumes = {"application/json"},
            produces = {"application/json"})
    public Author addAuthor(@RequestBody Author author) {
        return authorService.addAuthor(author);
    }

    @ResponseBody
    @GetMapping(produces = {"application/json"})
    public List<Author> getAllAuthors(
            @RequestParam(value = "authorName", required = false, defaultValue = "") String authorName,
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "to", required = false) Integer to
    ) {
        return authorService.getAll(authorName);
    }

    @ResponseBody
    @GetMapping(value = {"/{authorId}"},
            produces = {"application/json"})
    public Author getAuthor(@PathVariable String authorId) {
        return authorService.getAuthor(authorId);
    }

    @ResponseBody
    @DeleteMapping(value = {"/{authorId}"},
            consumes = {"application/json"})
    public void removeAuthor(@PathVariable String authorId) {
        authorService.deleteAuthor(authorId);
    }

    @ResponseBody
    @PutMapping(value = {"/{authorId}"},
            consumes = {"application/json"},
            produces = {"application/json"})
    public Author updateAuthor(@PathVariable String authorId, @RequestBody Author authorFromUser) {
        return authorService.updateAuthor(authorId, authorFromUser);
    }
}
