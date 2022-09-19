package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthorRepository {

    private List<Authors> authorList = new ArrayList();

    private String errorMessage = "The author you requested doesn't exist. Please review your parameters!";

    private int getAuthorIndexInAuthorList(String authorId) {
        for(int i = 0; i < authorList.size(); ++i) {
            Authors currentItem = authorList.get(i);
            if (currentItem.getId().equals(authorId)) {
                return i;
            }
        }
        return -1;
    }

    public Authors postAuthors(Authors author) {
        String authorId = UUID.randomUUID().toString();
        author.setId(authorId);
        authorList.add(author);
        return author;
    }

    public Authors getAuthors(String authorId) {
        if (getAuthorIndexInAuthorList(authorId) >= 0) {
            Authors author = authorList.get(getAuthorIndexInAuthorList(authorId));
            return author;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }
    }

    public void deleteAuthors(String authorId) {
        if (getAuthorIndexInAuthorList(authorId) >= 0) {
            authorList.remove(getAuthorIndexInAuthorList(authorId));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }
    }

    public Authors putAuthors(String authorId, Authors authorFromUser) {
        if (getAuthorIndexInAuthorList(authorId) >= 0) {
            authorFromUser.setId(authorId);
            authorList.set(getAuthorIndexInAuthorList(authorId), authorFromUser);
            return authorFromUser;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }
    }
}
