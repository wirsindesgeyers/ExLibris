package com.biblioteca_api.biblioteca.infra.dbseeding;

import com.biblioteca_api.biblioteca.entities.User;
import com.biblioteca_api.biblioteca.entities.UserRole;
import com.biblioteca_api.biblioteca.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, PasswordEncoder encoder) {
        return args -> {
            String adminEmail = "admin@exlibris.com";
            if (repository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(UserRole.ADMIN);

                repository.save(admin);

                System.out.println("--------------------------------------------------");
                System.out.println("SEED: Usuário ADMIN padrão criado com sucesso!");
                System.out.println("Email: " + adminEmail);
                System.out.println("Senha: admin123");
                System.out.println("--------------------------------------------------");
            }
        };
    }
}