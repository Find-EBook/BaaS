package com.sesac.baas.tenant.entity;


import com.sesac.baas.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String serviceName;



    @Builder.Default // 필드 초기화에 대한 기본 값을 설정합니다.
    @OneToMany(mappedBy = "tenant")
    private Set<User> users = new HashSet<>(); // ???

}
