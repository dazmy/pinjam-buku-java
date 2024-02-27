package com.dazmy.pinjam.buku.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private String name;
    private String username;
    private String dob;
    private String role;
    private String authorization;

}
