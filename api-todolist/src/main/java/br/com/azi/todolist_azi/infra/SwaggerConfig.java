package br.com.azi.todolist_azi.infra;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("To-Do List - Desafio técnico AZ Tecnologia em Gestão")
                        .description("API proposta como solução ao desafio técnico da AZ Tecnologia em Gestão, para a criação de um To-Do list")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Gustavo Colombo da Rocha")
                                .email("gusttavocolombo@gmail.com")
                        )
                );
    }
}
