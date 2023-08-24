package com.sesac.baas.jwt.filter;

import com.sesac.baas.jwt.repository.TokenBlackListRepository;
import com.sesac.baas.jwt.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;



// Lombok의 @RequiredArgsConstructor 어노테이션은 final 또는 @NonNull 필드만을 파라미터로 가지는 생성자를 자동으로 생성합니다.
@RequiredArgsConstructor
// Lombok의 @Slf4j 어노테이션은 이 클래스에 대한 Logger 변수를 자동으로 생성합니다.
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    // Spring의 의존성 주입을 위한 어노테이션입니다.
    @Autowired
    private UserDetailsService userDetailsService;

    private final TokenBlackListRepository tokenBlackListRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 필터의 의존성들을 주입받기 위한 생성자입니다.
    public JwtRequestFilter(UserDetailsService userDetailsService,
                            TokenBlackListRepository tokenBlackListRepository,
                            JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.tokenBlackListRepository = tokenBlackListRepository;
        this.jwtUtil = jwtUtil;
    }

    // OncePerRequestFilter의 doFilterInternal 메서드를 오버라이드 합니다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            // Authorization 헤더를 가져옵니다.
            final String authorizationHeader = request.getHeader("Authorization");

            String username = null;
            String jwt = null;

            // Authorization 헤더가 "Bearer "로 시작한다면, 토큰을 가져와 검증 및 파싱을 수행합니다.
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                // 토큰이 블랙리스트에 있는지 확인합니다.
                if (isBlacklisted(jwt)) {
                    log.warn("Token is blacklisted");
                    throw new Exception("Token is blacklisted.");
                }
                username = jwtUtil.getUsernameFromToken(jwt);
                log.info("Username extracted from token: {}", username);
            }

            // 토큰에서 유저명을 성공적으로 가져오고, 해당 사용자가 아직 인증되지 않았다면
            // 유저의 인증 상세 정보를 불러와 토큰을 검증하고, 시큐리티 컨텍스트에 설정합니다.
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                log.info("Validating token for user: {}", username);


                if (jwtUtil.validateToken(jwt, userDetails)) {
                    log.info("Token validated successfully for user: {}", username);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

                // 토큰이 새로고침이 필요한지 확인하고, 필요하다면 새 토큰을 생성하여 응답 헤더에 추가합니다.
                if (jwtUtil.shouldTokenBeRefreshed(jwt)) {
                    log.info("Token should be refreshed for user: {}", username);
                    String refreshedToken = jwtUtil.generateToken(userDetails);
                    response.setHeader("Authorization", "Bearer " + refreshedToken);
                    log.info("Token refreshed and set in response header for user: {}", username);
                }

            }
        } catch (ExpiredJwtException e) {
            log.warn("Token has expired for request: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token has expired");
        } catch (Exception e) {
            log.error("Unauthorized access: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + e.getMessage());
        }
        // 필터 체인을 계속 실행합니다.
        chain.doFilter(request, response);
    }

    // 주어진 토큰이 블랙리스트에 있는지 확인하는 메서드입니다.
    private boolean isBlacklisted(String token) {
        log.info("Checking if token is blacklisted");
        boolean blacklisted = tokenBlackListRepository.existsByToken(token);
        log.info("Token blacklist check result: {}", blacklisted ? "Blacklisted" : "Not Blacklisted");
        return blacklisted;
    }
}

