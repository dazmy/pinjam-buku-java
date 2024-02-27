package com.dazmy.pinjam.buku.repository;

import com.dazmy.pinjam.buku.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    boolean existsByUsername(String username);
}
