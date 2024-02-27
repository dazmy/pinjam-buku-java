package com.dazmy.pinjam.buku.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_borrow_books")
public class UserBorrowBook {

    @EmbeddedId
    private UserBorrowBookId id;

    @Column(name = "date_borrow")
    private LocalDate dateBorrow;

    @Column(name = "date_return")
    private LocalDate dateReturn;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", updatable = false, insertable = false, referencedColumnName = "id")
    private User user;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id", updatable = false, insertable = false, referencedColumnName = "id")
    private Book book;

}
