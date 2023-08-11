package com.sesac.baas.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class UserLoginRequest {

    private String email;

    private String password;

    private Integer tenant_id;

    @Builder
    public UserLoginRequest(final String email, final String password, final Integer tenant_id) {
        this.email = email;
        this.password = password;
        this.tenant_id = tenant_id;
    }
}
