package com.example.demo.web;

public interface AuthorRepository {
     Authors postAuthors(Authors author);

     Authors getAuthors(String authorId);

     void deleteAuthors(String authorId);

     Authors putAuthors(String authorId, Authors authorFromUser);
}
