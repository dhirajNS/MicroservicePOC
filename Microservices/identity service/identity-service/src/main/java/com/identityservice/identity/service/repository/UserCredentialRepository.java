package com.identityservice.identity.service.repository;

import com.identityservice.identity.service.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential,Integer> {

    UserCredential findByUsername(String username);
}
