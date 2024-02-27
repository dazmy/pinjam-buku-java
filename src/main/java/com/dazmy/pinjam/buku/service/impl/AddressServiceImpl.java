package com.dazmy.pinjam.buku.service.impl;

import com.dazmy.pinjam.buku.entity.Address;
import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.model.request.CreateAddressRequest;
import com.dazmy.pinjam.buku.model.response.AddressResponse;
import com.dazmy.pinjam.buku.repository.AddressRepository;
import com.dazmy.pinjam.buku.service.AddressService;
import com.dazmy.pinjam.buku.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ValidationService validationService;

    @Override
    @Transactional
    public void create(CreateAddressRequest request, User user) {
        validationService.validate(request);

        Address address = new Address();
        address.setUser(user);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());

        addressRepository.save(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAll(User user) {
        List<Address> addresses = addressRepository.findAllByUser(user);
        return addresses.stream().map(this::toAddressResponse).toList();
    }

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }

}
