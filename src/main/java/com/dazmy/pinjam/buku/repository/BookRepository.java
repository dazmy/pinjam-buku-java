package com.dazmy.pinjam.buku.repository;

import com.dazmy.pinjam.buku.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
