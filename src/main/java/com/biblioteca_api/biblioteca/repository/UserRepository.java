package com.biblioteca_api.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.biblioteca_api.biblioteca.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<UserDetails> findByEmail(String email);
}
