package com.example.demo.web;

import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping(value = {"/api/author"})
class AuthorController {

    @PostMapping(consumes = {"application/json"},
                produces = {"application/json"})
    Author addAuthor(@RequestBody Author author) {
        return DatenBank.postAuthors(author);
    }

    @ResponseBody
    @GetMapping(value = {"/{authorId}"},
                produces = {"application/json"})
    Author getAuthor(@PathVariable String authorId){
        return DatenBank.getAuthors(authorId);
    }

    @ResponseBody
    @DeleteMapping(value = {"/{authorId}"},
                   consumes = {"application/json"})
    void removeAuthor(@PathVariable String authorId){
        DatenBank.deleteAuthors(authorId);
    }

    @ResponseBody
    @PutMapping(value = {"/{authorId}"},
                consumes = {"application/json"},
                produces = {"application/json"})
    Author updateAuthor(@PathVariable String authorId, @RequestBody Author authorFromUser){
        return DatenBank.putAuthors(authorId, authorFromUser);
    }
}
