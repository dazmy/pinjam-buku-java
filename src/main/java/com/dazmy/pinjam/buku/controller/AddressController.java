package com.dazmy.pinjam.buku.controller;

import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.model.request.CreateAddressRequest;
import com.dazmy.pinjam.buku.model.response.AddressResponse;
import com.dazmy.pinjam.buku.model.response.CoreResponse;
import com.dazmy.pinjam.buku.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users/current/addresses", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CoreResponse<String>> create(@RequestBody CreateAddressRequest request, User user) {
        addressService.create(request, user);
        return ResponseEntity.status(201).body(CoreResponse.<String>builder().status(201).data("OK").build());
    }

    @GetMapping
    public ResponseEntity<CoreResponse<List<AddressResponse>>> getAll(User user) {
        List<AddressResponse> addressResponses = addressService.getAll(user);
        return ResponseEntity.ok().body(CoreResponse.<List<AddressResponse>>builder().status(200).data(addressResponses).build());
    }

}
