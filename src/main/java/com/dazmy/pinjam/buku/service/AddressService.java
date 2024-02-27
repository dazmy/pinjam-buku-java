package com.dazmy.pinjam.buku.service;

import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.model.request.CreateAddressRequest;
import com.dazmy.pinjam.buku.model.response.AddressResponse;

import java.util.List;

public interface AddressService {

    void create(CreateAddressRequest request, User user);

    List<AddressResponse> getAll(User user);

}
