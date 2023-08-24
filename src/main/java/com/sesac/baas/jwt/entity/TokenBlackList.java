package com.sesac.baas.jwt.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// @Entity 어노테이션은 JPA를 사용하여 이 클래스를 데이터베이스의 테이블로 표현하겠다는 것을 나타냅니다.
@Entity
// @Getter는 Lombok의 어노테이션으로, 각 필드에 대한 getter 메서드를 자동으로 생성해줍니다.
@Getter
// @NoArgsConstructor는 Lombok의 어노테이션으로, 파라미터가 없는 기본 생성자를 자동으로 생성해줍니다.
@NoArgsConstructor
// @Table 어노테이션은 이 엔티티가 매핑될 데이터베이스 테이블의 이름을 지정합니다. 여기서는 "token_blacklist"로 지정되었습니다.
@Table(name = "token_blacklist")
public class TokenBlackList {

    // @Id는 이 필드가 테이블의 기본 키(primary key)임을 나타냅니다.
    @Id
    // @GeneratedValue는 기본 키 생성 전략을 지정하는데 사용되며, 여기서는 데이터베이스의 IDENTITY를 사용하여 자동 증가하는 전략을 선택했습니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 토큰 문자열을 저장하기 위한 필드
    private String token;

    // @Builder는 Lombok의 어노테이션으로, 빌더 패턴의 코드를 자동으로 생성해줍니다.
    // 이 생성자는 빌더 패턴을 사용하여 객체를 생성할 때 사용됩니다.
    @Builder
    public TokenBlackList(String token) {
        this.token = token;
    }
}

