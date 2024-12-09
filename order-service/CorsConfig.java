package com.myecommerce.order_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Разрешить доступ ко всем эндпоинтам
                        .allowedOrigins("http://localhost:8084") // Разрешить доступ с фронта
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Разрешить только необходимые методы
                        .allowedHeaders("*") // Разрешить любые заголовки
                        .allowCredentials(true); // Разрешить отправку cookies
            }
        };
    }
}
