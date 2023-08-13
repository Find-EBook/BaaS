package com.sesac.baas.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 등록 요청에 사용되는 데이터 전송 객체(DTO).
 * 이 클래스는 클라이언트에서 전송되는 사용자 등록 관련 데이터를 표현합니다.
 */
@Getter  // Lombok 라이브러리를 사용하여 getter 메서드를 자동으로 생성합니다.
@NoArgsConstructor  // 인자가 없는 기본 생성자를 자동으로 생성합니다.
public class UserRegistrationRequest {

    // 사용자의 이메일 주소를 저장하는 필드입니다.
    private String email;

    // 사용자의 비밀번호를 저장하는 필드입니다.
    private String password;

    /**
     * 이 클래스의 빌더 패턴 스타일 생성자입니다.
     * @param email 사용자의 이메일 주소
     * @param password 사용자의 비밀번호
     */
    @Builder  // Lombok 라이브러리를 사용하여 빌더 패턴 스타일의 생성자 메서드를 자동으로 생성합니다.
    public UserRegistrationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}


