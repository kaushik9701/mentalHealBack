package com.mentalHeal.mentalHeal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // apply CORS to these routes
                        .allowedOrigins("http://localhost:5173","https://https://mentalheal-front.vercel.app/")
                        // allow frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // allow these HTTP methods
                        .allowedHeaders("*") // allow any headers
                        .allowCredentials(true); // allow cookies or authorization headers
            }
        };
    }
}
