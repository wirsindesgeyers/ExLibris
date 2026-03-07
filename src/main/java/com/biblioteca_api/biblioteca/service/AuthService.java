package com.biblioteca_api.biblioteca.service;

import com.biblioteca_api.biblioteca.dto.BookResponseDTO;
import com.biblioteca_api.biblioteca.dto.auth.LoginRequestDTO;
import com.biblioteca_api.biblioteca.dto.auth.LoginResponseDTO;
import com.biblioteca_api.biblioteca.dto.auth.RegisterRequestDTO;
import com.biblioteca_api.biblioteca.dto.auth.RegisterResponseDTO;
import com.biblioteca_api.biblioteca.entities.User;
import com.biblioteca_api.biblioteca.entities.UserRole;
import com.biblioteca_api.biblioteca.infra.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import com.biblioteca_api.biblioteca.infra.security.TokenService;
import com.biblioteca_api.biblioteca.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public LoginResponseDTO login(LoginRequestDTO requestDTO){
        var userAndPassword = new UsernamePasswordAuthenticationToken(requestDTO.email(), requestDTO.password());

        var auth = this.authenticationManager.authenticate(userAndPassword);

        var user = (User) auth.getPrincipal();

        var token = tokenService.generateToken(user);

        LoginResponseDTO response = new LoginResponseDTO(token, "Bearer", user.getRole().name());

        return response;
   }

    public RegisterResponseDTO register(RegisterRequestDTO requestDTO, UserRole forcedRole){

        if(this.userRepository.findByEmail(requestDTO.email()).isPresent()) throw new UserAlreadyExistsException("Usuário já fora criado.");

        String encryptedPassword = this.passwordEncoder.encode(requestDTO.password());

        UserRole finalRole = (forcedRole != null) ? forcedRole : UserRole.READER;

        User newUser = new User();
        newUser.setEmail(requestDTO.email());
        newUser.setPassword(encryptedPassword);
        newUser.setName(requestDTO.name());
        newUser.setRole(finalRole);

        userRepository.save(newUser);

        return RegisterResponseDTO.fromEntity(newUser);
    }

}
