package com.dazmy.pinjam.buku.controller;

import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.model.request.LoginRequest;
import com.dazmy.pinjam.buku.model.request.RegisterRequest;
import com.dazmy.pinjam.buku.model.request.ResetPasswordRequest;
import com.dazmy.pinjam.buku.model.response.CoreResponse;
import com.dazmy.pinjam.buku.model.response.LoginResponse;
import com.dazmy.pinjam.buku.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/register", consumes = APPLICATION_JSON_VALUE)
    public CoreResponse<String> register(@RequestBody RegisterRequest request, HttpServletResponse response) {
        authService.register(request);
        response.setStatus(201);
        return CoreResponse.<String>builder().status(201).data("OK").build();
    }

    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE)
    public CoreResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return CoreResponse.<LoginResponse>builder().status(200).data(loginResponse).build();
    }

    @DeleteMapping("/logout")
    public CoreResponse<String> logout(User user) {
        authService.logout(user);
        return CoreResponse.<String>builder().status(200).data("OK").build();
    }

    @PatchMapping(value = "/password/reset")
    public ResponseEntity<CoreResponse<LoginResponse>> resetPassword(@RequestBody ResetPasswordRequest request, User user) {
        LoginResponse loginResponse = authService.resetPassword(request, user);
        return ResponseEntity.ok().body(CoreResponse.<LoginResponse>builder().status(200).data(loginResponse).build());
    }

}
