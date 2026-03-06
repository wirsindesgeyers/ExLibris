package com.biblioteca_api.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.biblioteca_api.biblioteca.entities.User;
import com.biblioteca_api.biblioteca.factories.UserFactory;
import com.biblioteca_api.biblioteca.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    private User user;

    // Testes para loadUserByUsername

    @BeforeEach
    void setUp() {
        user = UserFactory.createValidUser();
    }

    @DisplayName("Deve retornar UserDetails corretamente")
    @Test
    void loadUserByUsernameSuccess() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails foundUser = authorizationService.loadUserByUsername(user.getEmail());

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getUsername());

        verify(userRepository, times(1)).findByEmail(user.getEmail());

    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o e-mail não for encontrado")
    void loadUserByUsername_UserNotFound_ThrowsException() {

        String fakeEmail = "fantasma@exlibris.com";
        when(userRepository.findByEmail(fakeEmail)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authorizationService.loadUserByUsername(fakeEmail);
        });

        assertTrue(exception.getMessage().contains(fakeEmail));

        verify(userRepository, times(1)).findByEmail(fakeEmail);
    }

}
