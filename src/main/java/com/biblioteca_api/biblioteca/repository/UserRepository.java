package com.biblioteca_api.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.biblioteca_api.biblioteca.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String email);
}
