package com.sesac.baas.user.controller;


import com.sesac.baas.jwt.dto.AuthenticationResponse;
import com.sesac.baas.jwt.entity.TokenBlackList;
import com.sesac.baas.jwt.repository.TokenBlackListRepository;
import com.sesac.baas.jwt.util.JwtUtil;
import com.sesac.baas.tenant.service.TenantService;
import com.sesac.baas.user.dto.UserLoginRequest;
import com.sesac.baas.user.dto.UserRegistrationRequest;
import com.sesac.baas.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// REST API 컨트롤러임을 나타내는 어노테이션입니다.
@RestController
// 필요한 종속성을 final로 선언하고 초기화하지 않게 하면, 생성자 주입을 사용하여 자동으로 초기화됩니다.
@RequiredArgsConstructor
// 로깅 기능을 위한 어노테이션입니다.
@Slf4j
// 이 컨트롤러의 모든 엔드포인트에 대한 기본 URL 경로입니다.
@RequestMapping("/api/user")
public class UserController {
    private final TenantService tenantService;
    private final TokenBlackListRepository tokenBlackListRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    /**
     * 사용자의 로그인 요청을 처리하는 메서드입니다.
     *
     * @param loginRequest 사용자의 이메일과 비밀번호 정보를 담고 있는 객체
     * @return JWT 토큰을 포함한 응답 혹은 에러 메시지
     */
    // 사용자 로그인을 처리하는 엔드포인트입니다.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest loginRequest) {
        // 로그인 요청에 대한 로그입니다.
        log.info("Login request for email: {}", loginRequest.getEmail());

        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());

        if (userDetails == null) {
            // 유효하지 않은 사용자에 대한 로그입니다.
            log.warn("User details is null for email: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        if (userService.checkPassword(loginRequest.getPassword(), userDetails.getPassword())) {
            // 토큰을 생성하고 응답으로 반환합니다.
            final String jwt = jwtUtil.generateToken(userDetails);
            log.info("Token generated for user: {}", loginRequest.getEmail());
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } else {
            // 비밀번호가 틀렸을 때의 로그입니다.
            log.warn("Invalid password for user: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    /**
     * 사용자의 회원가입 요청을 처리하는 메서드입니다.
     *
     * @param registrationRequest 사용자의 이메일, 이름, 비밀번호 등의 정보를 담고 있는 객체
     * @return 성공적인 회원가입 결과 혹은 에러 메시지
     */
    // 사용자 등록을 처리하는 엔드포인트입니다.
    @PostMapping("/register")
    public String register(@RequestBody UserRegistrationRequest registrationRequest) {
        log.info("Registration request for email: {}", registrationRequest.getEmail());

        if (userService.userExists(registrationRequest.getEmail())) {
            log.warn("User already exists with email: {}", registrationRequest.getEmail());
            return "User already exists";
        }

        // 테넌트가 존재하는지 검사합니다.
        if (!tenantService.existsById(registrationRequest.getTenantId())) {
            log.warn("Tenant does not exist with ID: {}", registrationRequest.getTenantId());
            return "Tenant does not exist";
        }
        // 사용자를 등록하는 서비스 메서드를 호출합니다.
        userService.registerUser(registrationRequest);
        return "User registered successfully";
    }


    /**
     * 사용자의 로그아웃 요청을 처리하는 메서드입니다.
     * 실제 로그아웃의 의미가 아니라, JWT 토큰을 블랙리스트에 추가하여 더 이상 사용할 수 없게 합니다.
     *
     * @param request 사용자의 요청 정보, JWT 토큰을 추출하기 위해 사용됩니다.
     * @return 로그아웃 처리 결과 혹은 에러 메시지
     */
    // 사용자 로그아웃을 처리하는 엔드포인트입니다.
    // 주의: 이는 실제 로그아웃과는 다르게 토큰을 블랙리스트에 추가하는 것을 의미합니다.
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = extractJwtFromRequest(request);
        if(token == null || tokenBlackListRepository.existsByToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token");
        }
        // 토큰을 블랙리스트에 추가합니다.
        tokenBlackListRepository.save(new TokenBlackList(token));
        return ResponseEntity.ok("Logged out successfully");
    }


    /**
     * HttpServletRequest 객체에서 "Authorization" 헤더를 추출하여 JWT 토큰을 반환하는 메서드입니다.
     *
     * @param request 사용자의 요청 정보
     * @return JWT 토큰 문자열. 토큰이 존재하지 않으면 null을 반환합니다.
     */
    // 요청 헤더에서 JWT 토큰을 추출하는 private 메서드입니다.
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}




