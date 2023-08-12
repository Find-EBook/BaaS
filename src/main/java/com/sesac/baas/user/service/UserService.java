package com.sesac.baas.user.service;

import com.sesac.baas.user.entity.Member;
import com.sesac.baas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;


    public boolean login(String userName, String password){
        Optional<Member> userOptional = userRepository.findByUserName(userName);
        if(userOptional.isPresent()){
            Member user = userOptional.get();
            boolean matched = user.getPassword().equals(password);
            log.info("User found: {}, password matched: {}", user.getUserName(), matched);
            return matched;
        }
        log.info("User not found for username: {}", userName);
        return false;
    }



}
