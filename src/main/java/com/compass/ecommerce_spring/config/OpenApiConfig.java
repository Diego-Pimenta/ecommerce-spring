package com.compass.ecommerce_spring.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("security", new SecurityScheme()
                        .name("security")
                        .description("Authentication required with bearer token")
                        .type(SecurityScheme.Type.HTTP)
                        .in(SecurityScheme.In.HEADER)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Demo Ecommerce API")
                        .description("API documentation for managing products, sales and users")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Diego Pimenta")
                                .url("https://github.com/Diego-Pimenta")
                                .email("diegopimenta888@gmail.com"))
                        .license(new License()
                                .name("Apache License")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
