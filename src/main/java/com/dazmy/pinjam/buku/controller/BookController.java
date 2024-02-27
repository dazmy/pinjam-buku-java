package com.dazmy.pinjam.buku.controller;

import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.model.request.AddBookRequest;
import com.dazmy.pinjam.buku.model.request.BorrowBookRequest;
import com.dazmy.pinjam.buku.model.response.BookResponse;
import com.dazmy.pinjam.buku.model.response.CoreResponse;
import com.dazmy.pinjam.buku.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping(value = "/api/v1/books", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CoreResponse<String>> addBook(@RequestBody AddBookRequest request, User user) {
        bookService.addBook(request);
        return ResponseEntity.status(201).body(CoreResponse.<String>builder().status(201).data("OK").build());
    }

    @GetMapping
    public ResponseEntity<CoreResponse<List<BookResponse>>> getAll(User user) {
        // later we need pagination
        List<BookResponse> bookResponses = bookService.getAll();
        return ResponseEntity.ok().body(CoreResponse.<List<BookResponse>>builder().status(200).data(bookResponses).build());
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<CoreResponse<BookResponse>> getBook(@PathVariable Long bookId, User user) {
        BookResponse bookResponse = bookService.getBook(bookId);
        return ResponseEntity.ok().body(CoreResponse.<BookResponse>builder().status(200).data(bookResponse).build());
    }

    @PostMapping(value = "/{bookId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CoreResponse<String>> borrowBook(@RequestBody BorrowBookRequest request, @PathVariable Long bookId, User user) {
        bookService.borrowBook(request, bookId, user);
        return ResponseEntity.status(201).body(CoreResponse.<String>builder().status(201).data("OK").build());
    }

}
