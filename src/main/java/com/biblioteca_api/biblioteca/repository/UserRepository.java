package com.biblioteca_api.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca_api.biblioteca.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
