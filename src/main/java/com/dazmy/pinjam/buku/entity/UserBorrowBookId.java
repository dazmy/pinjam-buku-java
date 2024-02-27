package com.dazmy.pinjam.buku.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBorrowBookId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "book_id")
    private Long bookId;

    @Override
    public int hashCode() {
        return (int) (userId + bookId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserBorrowBookId other) {
            return (other.userId.equals(this.userId)) && (other.bookId.equals(this.bookId));
        }
        return false;
    }

}
