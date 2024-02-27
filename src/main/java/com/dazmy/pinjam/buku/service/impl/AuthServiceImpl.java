package com.dazmy.pinjam.buku.service.impl;

import com.dazmy.pinjam.buku.entity.Credential;
import com.dazmy.pinjam.buku.entity.Role;
import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.model.request.LoginRequest;
import com.dazmy.pinjam.buku.model.request.RegisterRequest;
import com.dazmy.pinjam.buku.model.request.ResetPasswordRequest;
import com.dazmy.pinjam.buku.model.response.LoginResponse;
import com.dazmy.pinjam.buku.repository.CredentialRepository;
import com.dazmy.pinjam.buku.repository.UserRepository;
import com.dazmy.pinjam.buku.service.AuthService;
import com.dazmy.pinjam.buku.service.BasicAuthService;
import com.dazmy.pinjam.buku.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private final ValidationService validationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BasicAuthService basicAuthService;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        validationService.validate(request);

        if (credentialRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(400, "Username was taken", null);
        }

        // create user with age (auto)
        User user = new User(
                request.getName(),
                LocalDate.parse(request.getDob()),
                Role.valueOf(request.getRole()),
                false
        );

        userRepository.save(user);

        Credential credential = new Credential();
        credential.setId(user.getId());
        credential.setUsername(request.getUsername());
        credential.setPassword(passwordEncoder.encode(request.getPassword()));

        credentialRepository.save(credential);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        validationService.validate(request);

        // here, auto 401 if not authenticated
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByCredential_Username(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(401, "Username or password invalid", null));

        // Idempotent ?
        if (!user.getActive()) {
            user.setActive(true);
            userRepository.save(user);
        }

        String authorization = basicAuthService.toBasicAuth(request.getUsername(), request.getPassword());

        return toLoginResponse(user, authorization);
    }

    @Override
    @Transactional
    public void logout(User user) {
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public LoginResponse resetPassword(ResetPasswordRequest request, User user) {
        validationService.validate(request);

        if (!request.getUsername().equals(user.getCredential().getUsername())) {
            throw new ResponseStatusException(400, "Username not same", null);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getCredential().getPassword())) {
            throw new ResponseStatusException(400, "Old Password not match", null);
        }

        user.getCredential().setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        String authorization = basicAuthService.toBasicAuth(request.getUsername(), request.getNewPassword());

        return toLoginResponse(user, authorization);
    }

    private LoginResponse toLoginResponse(User user, String authorization) {
        return LoginResponse.builder()
                .name(user.getName())
                .username(user.getCredential().getUsername())
                .dob(user.getDob().toString())
                .role(user.getRole().name())
                .authorization(authorization)
                .build();
    }

}
