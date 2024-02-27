package com.dazmy.pinjam.buku.service;

import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.model.request.LoginRequest;
import com.dazmy.pinjam.buku.model.request.RegisterRequest;
import com.dazmy.pinjam.buku.model.request.ResetPasswordRequest;
import com.dazmy.pinjam.buku.model.response.LoginResponse;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void logout(User user);

    LoginResponse resetPassword(ResetPasswordRequest request, User user);

}
