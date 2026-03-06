package com.example.demoapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Demo API - User Management")
                        .version("1.0")
                        .description("Dokumentasi API untuk mengelola User dan Upload Foto Profil")
                        .contact(new Contact().name("Nama Kamu").email("email@kamu.com")));
    }
}