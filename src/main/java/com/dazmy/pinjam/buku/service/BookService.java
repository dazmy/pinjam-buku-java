package com.dazmy.pinjam.buku.service;

import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.model.request.AddBookRequest;
import com.dazmy.pinjam.buku.model.request.BorrowBookRequest;
import com.dazmy.pinjam.buku.model.response.BookResponse;

import java.util.List;

public interface BookService {

    void addBook(AddBookRequest request);

    List<BookResponse> getAll();

    BookResponse getBook(Long bookId);

    void borrowBook(BorrowBookRequest request, Long bookId, User user);

}
