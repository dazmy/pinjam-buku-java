package com.dazmy.pinjam.buku.service.impl;

import com.dazmy.pinjam.buku.service.BasicAuthService;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class BasicAuthServiceImpl implements BasicAuthService {

    @Override
    public String toBasicAuth(String username, String password) {
        String basic = username + ":" + password;
        return Base64.getEncoder().encodeToString(basic.getBytes());
    }

    @Override
    public String toDecodeAuth(String basicAuth) {
        byte[] decode = Base64.getDecoder().decode(basicAuth);
        return new String(decode);
    }

}
