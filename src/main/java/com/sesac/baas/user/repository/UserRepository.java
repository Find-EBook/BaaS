package com.sesac.baas.user.repository;


import com.sesac.baas.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUserName(String username);
}
