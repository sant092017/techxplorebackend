package com.techxplore.techxplorebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Arrays;

/**
 * WebConfig - Configuration for CORS, HTTP methods, and web-related settings.
 *
 * This class works for BOTH local development and production EC2 deployment:
 * - Local: Uses localhost:3000, localhost:4200 from application.yml
 * - EC2: Uses configured domain from application-prod.yml (environment variables)
 *
 * Configuration:
 * - CORS (Cross-Origin Resource Sharing) settings
 * - Allowed HTTP methods (GET, POST, PUT, DELETE, PATCH, OPTIONS)
 * - Allowed headers and credentials
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Allowed origins from application.yml or application-prod.yml
     * Default: localhost:3000, localhost:4200
     */
    @Value("${allowed.origins:http://localhost:3000,http://localhost:4200,http://127.0.0.1:3000,http://127.0.0.1:4200}")
    private String allowedOrigins;

    /**
     * Configure CORS settings for the application.
     * Reads allowed origins from Spring properties (environment-aware).
     *
     * @param registry CORS registry to configure allowed origins, methods, headers, etc.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = parseAllowedOrigins();

        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.OPTIONS.name()
                )
                .allowedHeaders(
                        "Content-Type",
                        "Authorization",
                        "X-Requested-With",
                        "Accept",
                        "X-CSRF-Token"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Parse comma-separated allowed origins from property string.
     * Handles both single and multiple origins.
     *
     * @return Array of allowed origins
     */
    private String[] parseAllowedOrigins() {
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            return new String[]{
                    "http://localhost:3000",
                    "http://localhost:4200",
                    "http://127.0.0.1:3000",
                    "http://127.0.0.1:4200"
            };
        }

        return Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }
}

