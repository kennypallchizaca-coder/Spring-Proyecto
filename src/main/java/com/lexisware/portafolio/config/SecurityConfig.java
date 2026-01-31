package com.lexisware.portafolio.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Configuración de seguridad con JWT - Define endpoints públicos/protegidos y autenticación stateless
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // Encoder de passwords con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de seguridad HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF (no necesario en API stateless)
                .csrf(csrf -> csrf.disable())

                // Permitir CORS
                .cors(cors -> {
                })

                // Sesiones stateless (sin cookies)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Autorización de endpoints
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (Auth y Public)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()

                        // Actuator - Health checks y monitoring
                        .requestMatchers("/actuator/**").permitAll()

                        // Swagger - Documentación de API
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // Endpoints públicos de lectura (GET) - Para que cualquiera vea el portafolio
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/portfolios/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()

                        // Endpoints protegidos (Escritura y otros) - Requieren autenticación
                        .requestMatchers("/api/users/**").authenticated()
                        .requestMatchers("/api/projects/**").authenticated()
                        .requestMatchers("/api/advisories/**").authenticated() // Asesorías siguen privadas por defecto
                        .requestMatchers("/api/portfolios/**").authenticated()
                        .requestMatchers("/api/files/**").authenticated() // Upload de archivos requiere autenticación

                        // Cualquier otro endpoint requiere autenticación
                        .anyRequest().authenticated())

                // Agregar filtro JWT antes del filtro de autenticación estándar
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
