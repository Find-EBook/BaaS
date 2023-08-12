package com.sesac.baas.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
<<<<<<< dev:src/main/java/com/sesac/baas/user/entity/User.java
@Table(name = "users")
public class User {
=======
@Getter
public class Member {
>>>>>>> feature(#2): 로그인 기능 구현:src/main/java/com/sesac/baas/user/entity/Member.java

    @Id
    private String email;

    private String password;

    @Builder
<<<<<<< dev:src/main/java/com/sesac/baas/user/entity/User.java
    public User(String email, String password) {
        this.email = email;
=======
    public Member(final String userName, final String password) {
        this.userName = userName;
>>>>>>> feature(#2): 로그인 기능 구현:src/main/java/com/sesac/baas/user/entity/Member.java
        this.password = password;
    }
}

