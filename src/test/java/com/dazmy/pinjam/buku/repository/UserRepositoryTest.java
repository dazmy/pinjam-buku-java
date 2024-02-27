package com.dazmy.pinjam.buku.repository;

import com.dazmy.pinjam.buku.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void testFindByUsername() {
        User user = userRepository.findByCredential_Username("adam").orElse(null);
        assertNotNull(user);
    }
}