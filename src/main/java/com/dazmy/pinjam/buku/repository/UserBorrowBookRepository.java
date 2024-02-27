package com.dazmy.pinjam.buku.repository;

import com.dazmy.pinjam.buku.entity.Book;
import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.entity.UserBorrowBook;
import com.dazmy.pinjam.buku.entity.UserBorrowBookId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBorrowBookRepository extends JpaRepository<UserBorrowBook, UserBorrowBookId> {
    Optional<UserBorrowBook> findByUserAndBook(User user, Book book);
}
