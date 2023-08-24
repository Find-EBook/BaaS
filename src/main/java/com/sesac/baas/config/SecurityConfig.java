package com.sesac.baas.config;

import com.sesac.baas.jwt.filter.JwtRequestFilter;
import com.sesac.baas.jwt.repository.TokenBlackListRepository;
import com.sesac.baas.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// @Configuration 어노테이션은 이 클래스가 Spring의 설정 클래스임을 나타냅니다.
@Configuration
// @EnableWebSecurity는 Spring Security 설정을 활성화하는 어노테이션입니다.
@EnableWebSecurity
// @Slf4j는 로깅을 위한 Lombok의 어노테이션입니다.
// 이 어노테이션을 사용하면 log 변수를 통해 로깅할 수 있습니다.
@Slf4j
// @RequiredArgsConstructor는 final 또는 @NonNull이 붙은 필드의 생성자를 자동으로 생성해주는 Lombok의 어노테이션입니다.
@RequiredArgsConstructor
public class SecurityConfig {

    // JWT 관련 유틸리티 클래스
    private final JwtUtil jwtUtil;
    // 사용자 정보와 관련된 서비스
    private final UserDetailsService userDetailsService;
    // 토큰 블랙리스트와 관련된 저장소
    private final TokenBlackListRepository tokenBlackListRepository;

    // JWT 요청 필터를 Bean으로 등록합니다.
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        // 필요한 의존성들을 생성자를 통해 주입받아 JwtRequestFilter 객체를 생성합니다.
        return new JwtRequestFilter(userDetailsService, tokenBlackListRepository, jwtUtil);
    }

    // SecurityFilterChain은 Spring Security에서 HTTP 요청 처리를 위한 필터의 체인을 정의합니다.
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("---------------filter chain config-----------------");
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .csrf(csrf -> csrf.disable())
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                .sessionManagement(sessionManagement ->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // JWT 요청 필터를 UsernamePasswordAuthenticationFilter 전에 추가합니다.
        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        // 최종 구성된 SecurityFilterChain 객체를 반환합니다.
        return http.build();
    }

    // 웹 보안 커스터마이저를 정의합니다. 특정 자원 경로에 대한 보안 제외를 설정합니다.
    // 이를 통해 정적 자원들(css, js 등)에 대한 요청은 Spring Security의 필터를 거치지 않게 합니다.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("------ web config---------");
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
