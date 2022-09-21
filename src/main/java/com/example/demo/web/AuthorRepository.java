package com.example.demo.web;

public interface AuthorRepository {
     Author addAuthor(Author author);

     Author getAuthor(String authorId);

     void deleteAuthor(String authorId);

     Author updateAuthor(String authorId, Author authorFromUser);
}
