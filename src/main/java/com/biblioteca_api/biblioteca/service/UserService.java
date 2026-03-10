package com.biblioteca_api.biblioteca.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca_api.biblioteca.dto.UpdatePasswordDTO;
import com.biblioteca_api.biblioteca.dto.UserResponseDTO;
import com.biblioteca_api.biblioteca.entities.User;
import com.biblioteca_api.biblioteca.infra.exceptions.PasswordAlreadyExistsException;
import com.biblioteca_api.biblioteca.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    protected void validateUserExists(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
    }

    public User findUserEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public UserResponseDTO getUserById(Long id) {
        User user = findUserEntity(id);
        return UserResponseDTO.fromEntity(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public void updatePassword(User loggedUser, UpdatePasswordDTO dto) {
        if (!passwordEncoder.matches(dto.oldPassword(), loggedUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha atual não está correta.");
        }

        if (passwordEncoder.matches(dto.newPassword(), loggedUser.getPassword())) {
            throw new PasswordAlreadyExistsException("A senha nova não pode ser igual a anterior.");
        }

        loggedUser.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(loggedUser);
    }

    @Transactional
    public void adminUpdatePassword(Long userId, String newPassword) {
        User user = findUserEntity(userId);

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new PasswordAlreadyExistsException("A senha nova não pode ser igual a anterior.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

    }

}
