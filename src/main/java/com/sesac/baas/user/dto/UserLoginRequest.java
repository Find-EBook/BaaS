package com.sesac.baas.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class UserLoginRequest {

    private String email;

    private String password;

    @Builder
    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
