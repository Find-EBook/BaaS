package com.sesac.baas.jwt.util;

import com.sesac.baas.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// Spring에서 컴포넌트로 관리되기 위한 어노테이션입니다. 이를 통해 DI(Dependency Injection)를 받을 수 있습니다.

@Component
public class JwtUtil {


    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    // JWT 토큰 서명에 사용되는 비밀키
    private final String secret = "KEY";
    // JWT 토큰의 만료 시간 (1주)
    private final long expiration = 604800L;

    // UserDetails 객체를 기반으로 JWT 토큰을 생성하는 메서드
    public String generateToken(UserDetails userDetails) {
        log.info("Generating JWT token for user: {}", userDetails.getUsername());
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        log.info("Token generated successfully for user: {}", userDetails.getUsername());
        return token;
    }

    // 주어진 JWT 토큰이 유효한지 검사하는 메서드
    public boolean validateToken(String token, UserDetails userDetails) {
        log.info("Validating token for user: {}", userDetails.getUsername());
        final String username = getUsernameFromToken(token);
        boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        log.info("Token validation result for user {}: {}", userDetails.getUsername(), isValid ? "Valid" : "Invalid");
        return isValid;
    }

    // 토큰에서 사용자 이름(주체)을 추출하는 메서드
    public String getUsernameFromToken(String token) {
        log.info("Extracting username from token");
        String username = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        log.info("Username extracted from token: {}", username);
        return username;
    }

    // 토큰이 만료되었는지 확인하는 메서드
    private Boolean isTokenExpired(String token) {
        log.info("Checking if token is expired");
        final Date expiration = getExpirationDateFromToken(token);
        boolean isExpired = expiration.before(new Date());
        log.info("Token expiration check result: {}", isExpired ? "Expired" : "Not Expired");
        return isExpired;
    }

    // 토큰에서 만료 날짜를 추출하는 메서드
    private Date getExpirationDateFromToken(String token) {
        log.info("Extracting expiration date from token");
        Date expirationDate = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
        log.info("Expiration date extracted from token: {}", expirationDate);
        return expirationDate;
    }

    // 토큰의 만료 시간이 절반 이상 경과했는지 확인하는 메서드.
    // 이런 메서드는 토큰 갱신 전략에 따라 필요할 수 있습니다.
    public Boolean shouldTokenBeRefreshed(String token) {
        log.info("Checking if token should be refreshed");
        final Date expiration = getExpirationDateFromToken(token);
        long halfExpirationDuration = (expiration.getTime() - System.currentTimeMillis()) / 2;
        boolean shouldRefresh = new Date().getTime() > expiration.getTime() - halfExpirationDuration;
        log.info("Token refresh check result: {}", shouldRefresh ? "Should Refresh" : "No need to Refresh");
        return shouldRefresh;
    }
}
