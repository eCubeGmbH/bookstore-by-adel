package com.example.demo.web;

import com.example.demo.model.Author;
import com.example.demo.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @Operation(summary = "Add new Author")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Added the Author",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Author.class)
                    )}
            )

    })
    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public Author addAuthor(@RequestBody Author author) {
        return authorService.addAuthor(author);
    }

    @Operation(summary = "Find All Authors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the Authors",
                    content = {@Content
                            (mediaType = "application/json",
                                    schema = @Schema(implementation = Author.class)
                            )}),
            @ApiResponse(responseCode = "400",
                    description = "parameters from and to must be greater than 0"),
            @ApiResponse(responseCode = "400",
                    description = "parameter from must be greater than to"),
            @ApiResponse(responseCode = "400",
                    description = "result can  contains maximum 1000 elements")
    })
    @ResponseBody
    @GetMapping(produces = {"application/json"})
    public List<Author> getAllAuthors(
            @RequestParam(value = "authorName", required = false, defaultValue = "") String authorName,
            @RequestParam(value = "from") int from,
            @RequestParam(value = "to") int to
    ) {
        if (from < 0 || to < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parameters from and to must be greater than 0");
        }
        if (from > to) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parameter from must be greater than to");
        }
        if (Math.abs(from - to) > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "result can  contains maximum 1000 elements");
        }
        return authorService.getAll(authorName, from, to);
    }

    @Operation(summary = "Find Author by it´s Id")
    @ApiResponse(responseCode = "200",
            description = "succeed",
            content = {@Content
                    (mediaType = "application/json",
                            schema = @Schema(implementation = Author.class))
            })
    @ResponseBody
    @GetMapping(value = {"/{authorId}"}, produces = {"application/json"})
    public Author getAuthor(@PathVariable String authorId) {
        return authorService.getAuthor(authorId);
    }

    @Operation(summary = "Delete Author")
    @ApiResponse(responseCode = "200",
            description = "succeed",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Author.class))
            })
    @ResponseBody
    @DeleteMapping(value = {"/{authorId}"}, consumes = {"application/json"})
    public void removeAuthor(@PathVariable String authorId) {
        authorService.deleteAuthor(authorId);
    }

    @Operation(summary = "Update Author")
    @ApiResponse(responseCode = "200",
            description = "Author has been deleted",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Author.class))
            })
    @ResponseBody
    @PutMapping(value = {"/{authorId}"}, consumes = {"application/json"}, produces = {"application/json"})
    public Author updateAuthor(@PathVariable String authorId, @RequestBody Author authorFromUser) {
        return authorService.updateAuthor(authorId, authorFromUser);
    }
}
