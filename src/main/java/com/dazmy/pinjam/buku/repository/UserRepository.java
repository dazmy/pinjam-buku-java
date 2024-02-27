package com.dazmy.pinjam.buku.repository;

import com.dazmy.pinjam.buku.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCredential_Username(String username);

}
