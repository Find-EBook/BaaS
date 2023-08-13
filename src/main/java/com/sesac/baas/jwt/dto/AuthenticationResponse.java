package com.sesac.baas.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// @Data는 Lombok의 어노테이션으로, getter, setter, equals, hashCode, toString 메서드를 자동으로 생성해줍니다.
@Data
// @AllArgsConstructor는 Lombok의 어노테이션으로, 모든 필드 값을 파라미터로 받는 생성자를 자동으로 생성해줍니다.
@AllArgsConstructor
public class AuthenticationResponse {

    // JWT 토큰을 담기 위한 필드. 로그인 성공 시 이 토큰을 클라이언트에게 전달할 수 있습니다.
    private String jwt;
}
