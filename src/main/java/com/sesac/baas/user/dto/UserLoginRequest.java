package com.sesac.baas.user.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
public class UserLoginRequest {

    private String email;

    private String password;

    private Integer tenant_id;

}
