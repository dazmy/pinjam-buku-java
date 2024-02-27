package com.dazmy.pinjam.buku.repository;

import com.dazmy.pinjam.buku.entity.Address;
import com.dazmy.pinjam.buku.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUser(User user);
}
