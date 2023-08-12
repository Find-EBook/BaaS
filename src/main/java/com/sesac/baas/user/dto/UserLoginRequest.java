package com.sesac.baas.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class UserLoginRequest {

    private String email;

    private String password;

    @Builder
    public UserLoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
