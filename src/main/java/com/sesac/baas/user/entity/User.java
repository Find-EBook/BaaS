package com.sesac.baas.user.entity;

import com.sesac.baas.tenant.entity.Tenant;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보를 표현하는 엔터티 클래스.
 * 데이터베이스의 'users' 테이블과 매핑됩니다.
 */
@Entity  // JPA 엔터티임을 선언하며, 이 클래스는 데이터베이스 테이블에 매핑됩니다.
@NoArgsConstructor  // 인자가 없는 기본 생성자를 자동으로 생성합니다.
@Getter  // Lombok 라이브러리를 사용하여 각 필드에 대한 getter 메서드를 자동으로 생성합니다.
@Table(name = "users")  // 데이터베이스의 'users' 테이블과 매핑됩니다.

public class User {

    @Id  // 데이터베이스의 기본 키(primary key)로 사용되는 필드를 선언합니다.
    private String email;  // 사용자의 이메일 주소를 저장하는 필드입니다. 이 필드는 기본 키로 사용됩니다.

    private String password;  // 사용자의 비밀번호를 저장하는 필드입니다.

    /**
     * 이 클래스의 빌더 패턴 스타일 생성자입니다.
     * @param email 사용자의 이메일 주소
     * @param password 사용자의 비밀번호
     */
    @Builder  // Lombok 라이브러리를 사용하여 빌더 패턴 스타일의 생성자 메서드를 자동으로 생성합니다.
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @ManyToOne
    Tenant tenant;
}


