package com.biblioteca_api.biblioteca.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca_api.biblioteca.dto.UserResponseDTO;
import com.biblioteca_api.biblioteca.entities.User;
import com.biblioteca_api.biblioteca.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public UserResponseDTO getUserById(Long id) {
        User user = findUserEntity(id);
        return UserResponseDTO.fromEntity(user);
    }
}
