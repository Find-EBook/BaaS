package com.sesac.baas.user.repository;


import com.sesac.baas.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User 엔터티에 대한 데이터베이스 접근을 처리하는 리포지토리 인터페이스.
 * JPA를 사용하여 데이터베이스 CRUD 연산을 자동으로 제공받습니다.
 */
@Repository  // 이 인터페이스가 Spring 데이터 리포지토리로 사용되도록 선언합니다. 이를 통해 CRUD 작업과 관련된 메서드를 자동으로 제공받게 됩니다.
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * 이메일 주소(또는 사용자명)을 기반으로 해당 사용자를 데이터베이스에서 찾습니다.
     *
     * @param username 사용자의 이메일 주소
     * @return 해당 이메일 주소를 가진 사용자의 Optional 객체. 사용자가 없으면 Optional.empty()를 반환합니다.
     */
    Optional<User> findByEmail(String username);
}

