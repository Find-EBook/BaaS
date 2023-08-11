package com.sesac.baas.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class User {

    @Id
    private String userName;

    private String password;

    @Builder
    public User(final String userName, final String password) {
        this.userName = userName;
        this.password = password;
    }
}

