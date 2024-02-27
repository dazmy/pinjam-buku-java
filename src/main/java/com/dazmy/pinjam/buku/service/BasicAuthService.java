package com.dazmy.pinjam.buku.service;

public interface BasicAuthService {

    String toBasicAuth(String username, String password);

    String toDecodeAuth(String basicAuth);
}
