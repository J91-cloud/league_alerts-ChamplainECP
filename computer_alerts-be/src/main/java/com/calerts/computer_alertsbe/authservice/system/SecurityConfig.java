package com.calerts.computer_alertsbe.authservice.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendDomain;

    @Value("${auth0.domain}")
    private String AUTH0_DOMAIN;


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveJwtDecoder jwtDecoder) {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchange -> exchange
                        // Completely public endpoints
                        .pathMatchers(HttpMethod.GET, "/api/v1/readers/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/articles/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/authors/**").permitAll()

                        // Endpoints requiring authentication
                        .pathMatchers(HttpMethod.POST, "/api/create/**").authenticated()
                        .pathMatchers(HttpMethod.PUT, "/api/update/**").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/api/delete/**").authenticated()

                        // Catch-all to require authentication for other endpoints
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder))
                )
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        String issuerUri = "https://" + AUTH0_DOMAIN + "/";
        return ReactiveJwtDecoders.fromOidcIssuerLocation(issuerUri);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(frontendDomain));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
                "authorization",
                "content-type",
                "x-requested-with"
        ));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}