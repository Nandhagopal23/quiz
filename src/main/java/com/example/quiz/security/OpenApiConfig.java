package com.example.quiz.security;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Quiz Management API",
                version = "v1.0",
                description = "A comprehensive RESTful API for managing online quizzes with user authentication, quiz creation, and submission scoring. "
                + "The API supports JWT-based authentication with token refresh capabilities, enabling secure access to quiz operations. "
                + "Users can create quizzes with multiple questions and options, retrieve quiz details, submit answers, and receive server-side score calculations.",
                contact = @Contact(
                        name = "Quiz API Support",
                        email = "support@quizapi.example.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
            @Server(
                    url = "http://localhost:8080",
                    description = "Local Development Server"
            ),
            @Server(
                    url = "https://api.quizapp.example.com",
                    description = "Production Server"
            )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER,
        description = "JWT Bearer token for API authentication. Obtain a token by logging in via /api/auth/login endpoint."
)
public class OpenApiConfig {
}
