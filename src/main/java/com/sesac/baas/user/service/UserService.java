package com.sesac.baas.user.service;

import com.sesac.baas.user.dto.UserRegistrationRequest;
import com.sesac.baas.user.entity.User;
import com.sesac.baas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 사용자 관련 서비스 클래스.
 * 사용자의 로그인, 등록, 존재 확인, Spring Security와 연동된 사용자 정보 로드 기능을 제공합니다.
 */
@Service  // 이 클래스가 Spring 서비스 컴포넌트로 사용되도록 선언합니다.
@RequiredArgsConstructor  // final 또는 @NonNull 필드만을 파라미터로 받는 생성자를 생성합니다. 이를 통해 의존성 주입을 받습니다.
@Slf4j  // 로깅을 위한 Slf4j 로거를 사용 가능하게 합니다.
public class UserService implements UserDetailsService {



    private final UserRepository userRepository;  // 사용자 저장소
    private final PasswordService passwordService;  // 비밀번호 관련 서비스

    /**
     * 사용자 로그인을 처리하는 메서드.
     *
     * @param userName 사용자 이름 (이메일).
     * @param password 사용자 비밀번호.
     * @return 비밀번호가 일치하면 true, 그렇지 않으면 false.
     */
    public boolean login(String userName, String password){

        // 로깅: 로그인 시도 시작
        log.info("Login attempt for user: {}", userName);

        // 사용자 이름으로 사용자 정보를 조회
        Optional<User> userOptional = userRepository.findByEmail(userName);

        if(userOptional.isPresent()){

            // 사용자가 존재하는 경우
            User user = userOptional.get();

            // 사용자가 입력한 비밀번호와 저장된 비밀번호가 일치하는지 확인
            boolean matched = user.getPassword().equals(password);

            if(matched){
                // 로깅: 비밀번호 일치
                log.info("User found: {}. Password matched successfully.", user.getEmail());
            } else {
                // 로깅: 비밀번호 불일치
                log.warn("User found: {}. Password did not match.", user.getEmail());
            }

            return matched;
        }

        // 로깅: 해당 사용자 이름으로 사용자 정보를 찾지 못함
        log.warn("User not found for username: {}", userName);
        return false;
    }


    /**
     * 사용자를 등록하는 메서드.
     *
     * @param registrationRequest 사용자 등록에 필요한 정보를 포함하는 요청 객체.
     */
    public void registerUser(UserRegistrationRequest registrationRequest) {

        // 로깅: 사용자 등록을 시작한다는 메시지 출력
        log.info("Registering user with email: {}", registrationRequest.getEmail());

        // 사용자 등록 요청에서 정보를 추출해 새로운 User 객체 생성 (비밀번호는 암호화됨)
        User user = new User(registrationRequest.getEmail(), passwordService.encodePassword(registrationRequest.getPassword()));

        try {
            // 새로운 사용자 정보를 데이터베이스에 저장
            userRepository.save(user);

            // 로깅: 사용자가 성공적으로 등록되었다는 메시지 출력
            log.info("User registered successfully with email: {}", registrationRequest.getEmail());
        } catch (Exception e) {
            // 로깅: 사용자 등록 중 오류 발생 시 로그 출력
            log.error("Error occurred while registering user with email: {}", registrationRequest.getEmail(), e);
        }
    }

    /**
     * 주어진 이메일을 가진 사용자가 존재하는지 확인합니다.
     *
     * @param email 확인할 이메일
     * @return 사용자 존재 여부
     */
    public boolean userExists(String email) {
        log.info("Attempting to check if user exists with email: {}", email);

        boolean exists = userRepository.findByEmail(email).isPresent();

        if (exists) {
            log.info("User with email: {} exists.", email); // 사용자가 존재하는 경우의 로그
        } else {
            log.warn("No user found with email: {}", email); // 사용자가 존재하지 않는 경우의 로그
        }

        return exists;
    }


    /**
     * Spring Security와 연동된 사용자 정보 로드 메서드.
     *
     * @param username 로드할 사용자의 이메일
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 사용자를 찾지 못한 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: {}", username);

        // 사용자를 데이터베이스에서 조회
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.error("Failed to find user with email: {}. Throwing UsernameNotFoundException.", username); // 로그 추가
                    return new UsernameNotFoundException("User not found with email: " + username);
                });

        log.info("Successfully found user with email: {}", user.getEmail());

        // Spring Security에 사용할 UserDetails 객체 생성
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
        log.debug("Generated UserDetails for user with email: {}", user.getEmail()); // 로그 추가

        return userDetails;
    }


    /**
     * 주어진 원본 비밀번호와 인코딩된 비밀번호가 일치하는지 확인합니다.
     *
     * @param rawPassword 원본 비밀번호
     * @param encodedPassword 인코딩된 비밀번호
     * @return 비밀번호 일치 여부
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        log.info("Checking if raw password matches with encoded password.");
        boolean matches = passwordService.matches(rawPassword, encodedPassword);

        if(matches) {
            log.info("Raw password matches with encoded password.");
        } else {
            log.warn("Raw password does not match with encoded password.");
        }

        return matches;
    }
}
