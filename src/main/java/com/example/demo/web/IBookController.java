package com.example.demo.web;

import com.example.demo.model.Book;
import com.example.demo.model.BooksEnvelopDto;
import com.example.demo.model.enums.SortOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import java.util.Optional;

public interface IBookController {
    @Operation(summary = "Add new Book")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Added the Book",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Book.class)
            )}
        )
    })
    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    Book addAuthor(@Valid @RequestBody Book book);

    @Operation(summary = "Get All Books",
        description = "Retrieves a paginated list of books with optional sorting and filtering by name.",
        parameters = {
            @Parameter(name = "pageNumber",
                description = "The page number to retrieve. Must be greater than or equal to 0.",
                required = true, in = ParameterIn.QUERY,
                schema = @Schema(type = "integer", minimum = "0")),
            @Parameter(name = "pageSize",
                description = "The number of books to retrieve per page. Must be between 1 and 1000, inclusive.",
                required = true, in = ParameterIn.QUERY,
                schema = @Schema(type = "integer", minimum = "1", maximum = "1000")),
            @Parameter(name = "sortField",
                description = "The field by which to sort the books.",
                in = ParameterIn.QUERY,
                schema = @Schema(type = "string", defaultValue = "name", allowableValues = {"NAME", "AUTHORID", "PUBLISH_DATE"})),
            @Parameter(name = "sortOrder",
                description = "The order in which to sort the books.",
                in = ParameterIn.QUERY,
                schema = @Schema(type = "string", defaultValue = "asc", allowableValues = {"ASC", "DESC"})),
            @Parameter(name = "maybeBookName",
                description = "An optional parameter to filter books by name")
        })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully retrieved list of books",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Book.class))),
        @ApiResponse(responseCode = "400",
            description = "Invalid input parameters",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400",
            description = "parameters pageNumber and pageSize must be greater than 0"),
        @ApiResponse(responseCode = "400",
            description = "parameter pageNumber must be greater than pageSize"),
        @ApiResponse(responseCode = "400",
            description = "result can  contains maximum 1000 elements")
    })
    @GetMapping(produces = {"application/json"})
    BooksEnvelopDto getBooks(
        @Min(value = 0, message = "Parameter pageNumber must be greater or equal 0")
        @RequestParam(value = "pageNumber") int pageNumber,
        @Min(value = 1, message = "Parameter pageSize must be greater or equal 1")
        @Max(value = 1000, message = "Parameter pageSize must be less than 1000")
        @RequestParam(value = "pageSize") int pageSize,
        @RequestParam(value = "sortField", defaultValue = "name") BooksEnvelopDto.SortField sortField,
        @RequestParam(value = "sortOrder", defaultValue = "asc") SortOrder sortOrder,
        @RequestParam(value = "authorName", required = false) Optional<String> maybeBookName
    );

    @Operation(summary = "Find Book by it´s Id")
    @ApiResponse(responseCode = "200",
        description = "succeed",
        content = {@Content
            (mediaType = "application/json",
                schema = @Schema(implementation = Book.class))
        })
    @GetMapping(value = {"/{bookId}"}, produces = {"application/json"})
    Book getBook(@PathVariable long bookId);

    @Operation(summary = "Delete Book")
    @ApiResponse(responseCode = "200",
        description = "succeed",
        content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Book.class))
        })
    @DeleteMapping(value = {"/{bookId}"}, consumes = {"application/json"})
    void removeBook(@PathVariable long bookId);

    @Operation(summary = "Update Book")
    @ApiResponse(responseCode = "200",
        description = "Book has been deleted",
        content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Book.class))
        })
    @PutMapping(value = {"/{bookId}"}, consumes = {"application/json"}, produces = {"application/json"})
    Book updateBook(@PathVariable long bookId, @Valid @RequestBody Book bookFromUser);
}
