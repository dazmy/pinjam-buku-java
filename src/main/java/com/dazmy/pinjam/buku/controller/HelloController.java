package com.dazmy.pinjam.buku.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-api")
public class HelloController {

    @GetMapping
    public String hello(Authentication authentication) {
        return "Hello " + authentication.getName() + "!";
    }

}
