package com.example.demo.web;

import com.example.demo.model.Author;
import com.example.demo.model.enums.SortField;
import com.example.demo.model.enums.SortOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;

public interface IAuthorController {
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
    Author addAuthor(@Valid @RequestBody Author author);

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found the Authors",
            content = {@Content
                (mediaType = "application/json",
                    schema = @Schema(implementation = Author.class)
                )}),
        @ApiResponse(responseCode = "400",
            description = "parameters pageNumber and pageSize must be greater than 0"),
        @ApiResponse(responseCode = "400",
            description = "parameter pageNumber must be greater than pageSize"),
        @ApiResponse(responseCode = "400",
            description = "result can  contains maximum 1000 elements")
    })

    @Operation()
    @GetMapping(produces = {"application/json"})
    List<Author> getAllAuthors(
        @Min(value = 0, message = "Parameter pageNumber must be greater or equal 0")
        @RequestParam(value = "pageNumber") int pageNumber,
        @Min(value = 1, message = "Parameter pageSize must be greater or equal 1")
        @Max(value = 1000, message = "Parameter pageSize must be less than 1000")
        @RequestParam(value = "pageSize") int pageSize,
        @RequestParam(value = "sortField", defaultValue = "name") SortField sortField,
        @RequestParam(value = "sortOrder", defaultValue = "asc") SortOrder sortOrder,
        @RequestParam(value = "authorName", required = false) Optional<String> maybeAuthorName
    );

    @Operation(summary = "Find Author by it´s Id")
    @ApiResponse(responseCode = "200",
        description = "succeed",
        content = {@Content
            (mediaType = "application/json",
                schema = @Schema(implementation = Author.class))
        })
    @GetMapping(value = {"/{authorId}"}, produces = {"application/json"})
    Author getAuthor(@PathVariable String authorId);

    @Operation(summary = "Delete Author")
    @ApiResponse(responseCode = "200",
        description = "succeed",
        content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Author.class))
        })
    @DeleteMapping(value = {"/{authorId}"}, consumes = {"application/json"})
    void removeAuthor(@PathVariable String authorId);

    @Operation(summary = "Update Author")
    @ApiResponse(responseCode = "200",
        description = "Author has been deleted",
        content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Author.class))
        })
    @PutMapping(value = {"/{authorId}"}, consumes = {"application/json"}, produces = {"application/json"})
    Author updateAuthor(@PathVariable String authorId, @Valid @RequestBody Author authorFromUser);
}
