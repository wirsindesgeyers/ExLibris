package com.biblioteca_api.biblioteca.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca_api.biblioteca.dto.UpdatePasswordDTO;
import com.biblioteca_api.biblioteca.dto.UserResponseDTO;
import com.biblioteca_api.biblioteca.entities.User;
import com.biblioteca_api.biblioteca.factories.UserFactory;
import com.biblioteca_api.biblioteca.infra.exceptions.PasswordAlreadyExistsException;
import com.biblioteca_api.biblioteca.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    User user;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        user = UserFactory.createValidUser();
    }

    // Testes pra validateUserExists

    @DisplayName("Deve validar se usuário existe com sucesso")
    @Test
    void validateUserExistsSuccess() {

        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.validateUserExists(1L));

        verify(userRepository, times(1)).existsById(1L);

    }

    @DisplayName("Deve lançar exceção quando usuário não existir")
    @Test
    void validateUserExistsFail() {

        when(userRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.validateUserExists(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(userRepository, times(1)).existsById(1L);

    }

    // Testes pra findUserEntity

    @DisplayName("Deve retornar o usuário com sucesso")
    @Test
    void findUserEntitySuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserEntity(1L);

        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getId(), result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @DisplayName("Deve lançar ResponseStatusException (404) quando usuário não existe")
    @Test
    void findUserEntityFailed() {

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.findUserEntity(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(userRepository, times(1)).findById(99L);
    }

    // Testes pra getUserById

    @DisplayName("Deve retornar DTO de usuário com sucesso")
    @Test
    void getUserByIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getId(), response.id());

        verify(userRepository, times(1)).findById(1L);
    }

    // Testes pra getAllUsers

    @DisplayName("Deve retornar uma lista vazia de usuários com sucesso")
    @Test
    void getAllUsersEmptySuccess() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponseDTO> list = userService.getAllUsers();

        assertNotNull(list);
        assertTrue(list.isEmpty());

        verify(userRepository, times(1)).findAll();

    }

    @DisplayName("Deve retornar uma lista populada de usuários (DTO) com sucesso")
    @Test
    void getAllUsersWithDataSuccess() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> list = userService.getAllUsers();

        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());

        assertEquals(user.getId(), list.get(0).id());
        assertEquals(user.getName(), list.get(0).name());
        assertEquals(user.getEmail(), list.get(0).email());

        verify(userRepository, times(1)).findAll();
    }

    // Testes para updatePassword

    @DisplayName("Deve alterar a senha com sucesso")
    @Test
    void updatePasswordSuccess() {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO(user.getPassword(), "novaSenha");
        when(passwordEncoder.matches(updatePasswordDTO.oldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(updatePasswordDTO.newPassword(), user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(updatePasswordDTO.newPassword())).thenReturn("senhaHasheada");

        userService.updatePassword(user, updatePasswordDTO);

        assertEquals(user.getPassword(), "senhaHasheada");

        verify(userRepository, times(1)).save(user);
    }

    @DisplayName("Deve lançar um ResponseStatusException (400) quando a senha antiga não coincidir")
    @Test
    void updatePasswordBadRequest() {
        UpdatePasswordDTO dto = new UpdatePasswordDTO("senhaErrada", "novaSenha");
        when(passwordEncoder.matches(dto.oldPassword(), user.getPassword())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.updatePassword(user, dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A senha atual não está correta.", exception.getReason());

        verify(userRepository, never()).save(any());
    }

    @DisplayName("Deve lançar PasswordAlreadyExistsException quando a nova senha for igual a anterior")
    @Test
    void updatePasswordSameAsOldPassword() {
        UpdatePasswordDTO dto = new UpdatePasswordDTO("senhaAntiga", "senhaAntiga");

        when(passwordEncoder.matches(dto.oldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(dto.newPassword(), user.getPassword())).thenReturn(true);

        PasswordAlreadyExistsException exception = assertThrows(PasswordAlreadyExistsException.class, () -> {
            userService.updatePassword(user, dto);
        });

        assertEquals("A senha nova não pode ser igual a anterior.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    // Testes para adminUpdatePassword

    @DisplayName("Deve alterar a senha com sucesso pelo Admin")
    @Test
    void adminUpdatePasswordSuccess() {
        String newPassword = "novaSenhaAdmin";

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(newPassword)).thenReturn("hashAdminSeguro");

        assertDoesNotThrow(() -> userService.adminUpdatePassword(1L, newPassword));

        assertEquals("hashAdminSeguro", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @DisplayName("Deve lançar PasswordAlreadyExistsException quando o Admin tentar colocar a mesma senha")
    @Test
    void adminUpdatePasswordSameAsOldPassword() {
        String newPassword = "senhaAntigaDoUsuario";

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(true);

        PasswordAlreadyExistsException exception = assertThrows(PasswordAlreadyExistsException.class, () -> {
            userService.adminUpdatePassword(1L, newPassword);
        });

        assertEquals("A senha nova não pode ser igual a anterior.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
