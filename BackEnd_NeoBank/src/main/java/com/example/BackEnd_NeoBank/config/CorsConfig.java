package com.example.BackEnd_NeoBank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Configurações do CORS
        config.setAllowCredentials(true); // Permitir envio de cookies/autenticação
        config.addAllowedOriginPattern("*"); // Permitir todas as origens (ou especifique)
        config.addAllowedHeader("*"); // Permitir todos os cabeçalhos
        config.addAllowedMethod("*"); // Permitir todos os métodos HTTP

        source.registerCorsConfiguration("/**", config); // Aplicar a todos os endpoints

        return new CorsFilter(source);
    }
}
