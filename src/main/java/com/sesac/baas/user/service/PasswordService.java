package com.sesac.baas.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 비밀번호 관련 서비스 클래스.
 * 비밀번호의 인코딩과 인코딩된 비밀번호와 원본 비밀번호의 일치 여부 확인 기능을 제공합니다.
 */
@Service  // 이 클래스가 Spring 서비스 컴포넌트로 사용되도록 선언합니다.
@RequiredArgsConstructor  // final 또는 @NonNull 필드만을 파라미터로 받는 생성자를 생성합니다. 이를 통해 의존성 주입을 받습니다.
@Slf4j  // 로깅을 위한 Slf4j 로거를 사용 가능하게 합니다.
public class PasswordService {

    private final PasswordEncoder passwordEncoder;  // 비밀번호 인코딩을 위한 인코더

    /**
     * 주어진 원본 비밀번호를 인코딩합니다.
     *
     * @param rawPassword 인코딩되지 않은 원본 비밀번호
     * @return 인코딩된 비밀번호
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 주어진 원본 비밀번호와 인코딩된 비밀번호가 일치하는지 확인합니다.
     *
     * @param rawPassword 확인할 원본 비밀번호
     * @param encodedPassword 비교할 인코딩된 비밀번호
     * @return 두 비밀번호가 일치하면 true, 그렇지 않으면 false
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}


// SecurityConfig와 UserService같의 순환 참조가 발생하여 암호처리 로직을 따로 분리합니다.