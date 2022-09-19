package com.example.demo.web;

import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping(value = {"/api/author"})
class AuthorController {

    final AuthorRepository repository;

    AuthorController() {
        this.repository = new AuthorRepositoryListImpl();
    }

    @PostMapping(consumes = {"application/json"},
                produces = {"application/json"})
    Authors addAuthor(@RequestBody Authors author) {
        return repository.postAuthors(author);
    }

    @ResponseBody
    @GetMapping(value = {"/{authorId}"},
                produces = {"application/json"})
    Authors getAuthor(@PathVariable String authorId){
        return repository.getAuthors(authorId);
    }

    @ResponseBody
    @DeleteMapping(value = {"/{authorId}"},
                   consumes = {"application/json"})
    void removeAuthor(@PathVariable String authorId){
        repository.deleteAuthors(authorId);
    }

    @ResponseBody
    @PutMapping(value = {"/{authorId}"},
                consumes = {"application/json"},
                produces = {"application/json"})
    Authors updateAuthor(@PathVariable String authorId, @RequestBody Authors authorFromUser){
        return repository.putAuthors(authorId, authorFromUser);
    }
}
