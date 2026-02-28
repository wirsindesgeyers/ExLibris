package com.biblioteca_api.biblioteca.infra.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "ExLibris", version = "1.0.0", description = "API para gerenciamento de biblioteca e rede social interativa com reviews, ratings e tracking de leitura", contact = @Contact(name = "Kauan Maia Gomes", email = "kauanmaiagomes1307@gmail.com")))
public class SwaggerConfig {
}
