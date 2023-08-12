package com.sesac.baas.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    private String email;

    private String password;

    @Builder
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

