package com.sesac.baas.jwt.repository;

import com.sesac.baas.jwt.entity.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {
    boolean existsByToken(String token);
}
/*
* 로그아웃을 구현하기 위해 JWT 토큰 블랙리스트를 사용하겠습니다.
* 사용자가 로그아웃을 할 때, 현재 사용 중인 JWT 토큰을 블랙리스트에 추가합니다.
* 그런 다음, 요청을 처리할 때마다 블랙리스트에 해당 토큰이 있는지 확인합니다.
* */