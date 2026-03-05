package com.biblioteca_api.biblioteca.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca_api.biblioteca.dto.UserResponseDTO;
import com.biblioteca_api.biblioteca.entities.User;
import com.biblioteca_api.biblioteca.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("kauanmaiagomes@gmail.com");
        user.setPassword("12345");
        user.setName("kauan");

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
}
