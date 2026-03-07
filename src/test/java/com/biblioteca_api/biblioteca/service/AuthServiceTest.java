package com.biblioteca_api.biblioteca.service;

import com.biblioteca_api.biblioteca.dto.auth.LoginRequestDTO;
import com.biblioteca_api.biblioteca.dto.auth.LoginResponseDTO;
import com.biblioteca_api.biblioteca.dto.auth.RegisterRequestDTO;
import com.biblioteca_api.biblioteca.dto.auth.RegisterResponseDTO;
import com.biblioteca_api.biblioteca.entities.User;
import com.biblioteca_api.biblioteca.entities.UserRole;
import com.biblioteca_api.biblioteca.factories.UserFactory;
import com.biblioteca_api.biblioteca.infra.exceptions.UserAlreadyExistsException;
import com.biblioteca_api.biblioteca.infra.security.TokenService;
import com.biblioteca_api.biblioteca.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    private User validUser;
    private RegisterRequestDTO registerDTO;

    @BeforeEach
    void setup() {
        validUser = UserFactory.createValidUser();

        registerDTO = new RegisterRequestDTO(
                validUser.getName(),
                validUser.getEmail(),
                "password123",
                validUser.getRole().name()
        );
    }

    @Nested
    @DisplayName("Cenários de Autenticação (Login)")
    class LoginScenarios {

        @Test
        @DisplayName("Deve retornar token JWT e role quando login for bem-sucedido")
        void shouldReturnLoginResponseWhenCredentialsAreValid() {

            LoginRequestDTO loginDTO = new LoginRequestDTO(validUser.getEmail(), "password123");
            Authentication auth = mock(Authentication.class);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
            when(auth.getPrincipal()).thenReturn(validUser);
            when(tokenService.generateToken(validUser)).thenReturn("token-gerado-pela-api");


            LoginResponseDTO response = authService.login(loginDTO);

            assertAll("Verificação da Resposta de Login",
                    () -> assertNotNull(response),
                    () -> assertEquals("token-gerado-pela-api", response.token()),
                    () -> assertEquals(validUser.getRole().name(), response.role())
            );
            verify(tokenService).generateToken(validUser);
        }

        @Test
        @DisplayName("Deve propagar erro quando o AuthenticationManager falhar")
        void shouldThrowExceptionWhenAuthenticationFails() {
            when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Falha na autenticação"));

            assertThrows(BadCredentialsException.class, () -> authService.login(new LoginRequestDTO("wrong@email.com", "123")));
        }
    }

    @Nested
    @DisplayName("Cenários de Cadastro (Register)")
    class RegisterScenarios {

        @Test
        @DisplayName("Deve salvar novo usuário e retornar DTO de sucesso")
        void shouldSaveUserSuccessfully() {

            when(userRepository.findByEmail(registerDTO.email())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(registerDTO.password())).thenReturn("encoded_password");


            RegisterResponseDTO response = authService.register(registerDTO, null);


            assertNotNull(response);
            assertEquals(registerDTO.email(), response.email());

            verify(passwordEncoder).encode(registerDTO.password());
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Deve lançar UserAlreadyExistsException se o e-mail já constar no banco")
        void shouldThrowExceptionIfEmailAlreadyExists() {

            when(userRepository.findByEmail(registerDTO.email())).thenReturn(Optional.of(validUser));


            assertThrows(UserAlreadyExistsException.class, () -> authService.register(registerDTO, null));
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Deve respeitar a Role forçada mesmo que o DTO peça outra")
        void shouldOverrideRoleWhenForcedRoleIsProvided() {

            when(userRepository.findByEmail(registerDTO.email())).thenReturn(Optional.empty());


            RegisterResponseDTO response = authService.register(registerDTO, UserRole.ADMIN);


            assertEquals("ADMIN", response.role());
        }
    }
}