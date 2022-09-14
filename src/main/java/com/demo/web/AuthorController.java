package com.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(
        value = {"/api/author"},
        consumes = {"application/json"},
        produces = {"application/json"}
)
class AuthorController {
    private List<Author> authorList = new ArrayList();
    public String errorMessage = "The author you requested doesn't exist. Please review your parameters!";

    @PostMapping
    public Author addAuthor(@RequestBody Author author) {
        String authorId = UUID.randomUUID().toString();
        author.setId(authorId);
        this.authorList.add(author);
        return author;
    }

    private int getAuthorIndexInAuthorList(String authorId) {
        for(int i = 0; i < this.authorList.size(); ++i) {
            Author currentItem = this.authorList.get(i);
            if (currentItem.getId().equals(authorId)) {
                return i;
            }
        }

        return -1;
    }

    @GetMapping({"/{authorId}"})
    @ResponseBody
    Author getAuthor(@PathVariable String authorId) {
        if (this.getAuthorIndexInAuthorList(authorId) >= 0) {
            Author author = this.authorList.get(this.getAuthorIndexInAuthorList(authorId));
            return author;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, this.errorMessage);
        }
    }

    @DeleteMapping({"/{authorId}"})
    @ResponseBody
    void removeAuthor(@PathVariable String authorId) {
        if (this.getAuthorIndexInAuthorList(authorId) >= 0) {
            this.authorList.remove(this.getAuthorIndexInAuthorList(authorId));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, this.errorMessage);
        }
    }

    @PutMapping({"/{authorId}"})
    @ResponseBody
    Author updateAuthor(@PathVariable String authorId, @RequestBody Author authorFromUser) {
        if (this.getAuthorIndexInAuthorList(authorId) >= 0) {
            authorFromUser.setId(authorId);
            this.authorList.set(this.getAuthorIndexInAuthorList(authorId), authorFromUser);
            return authorFromUser;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, this.errorMessage);
        }
    }
}
