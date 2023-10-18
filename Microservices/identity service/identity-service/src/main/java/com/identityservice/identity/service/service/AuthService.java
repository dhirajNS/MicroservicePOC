package com.identityservice.identity.service.service;

import com.identityservice.identity.service.config.AuthenticationConfig;
import com.identityservice.identity.service.entity.UserCredential;
import com.identityservice.identity.service.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthService {

    @Autowired
    JwtService jwtService;
    @Autowired
    UserCredentialRepository userCredentialRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String saveUser(UserCredential userCredential){
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        userCredentialRepository.save(userCredential);
        return "user added to the system";
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> roles) {
        return jwtService.generateToken(username,roles);
    }


    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

}
