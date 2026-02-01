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

// Configuración principal de Spring Security - Define la política de acceso y cadena de filtros
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // Bean para el algoritmo de hash de contraseñas (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de la cadena de filtros de seguridad HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF ya que la autenticación es vía Token (Stateless)
                .csrf(csrf -> csrf.disable())

                // Configuración de CORS basada en el bean definido en CorsConfig
                .cors(cors -> {
                })

                // Establecer política de sesión como STATELESS (sin estado ni cookies)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Definición de reglas de autorización por ruta y método
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas de autenticación y recursos públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()

                        // Endpoints de monitoreo del sistema (Actuator)
                        .requestMatchers("/actuator/**").permitAll()

                        // Documentación técnica (Swagger/OpenAPI)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/api-docs/**")
                        .permitAll()

                        // Permisos de lectura global para perfiles y proyectos
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/portfolios/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()

                        // Rutas que requieren autenticación explícita (Escritura/Gestión)
                        .requestMatchers("/api/users/**").authenticated()
                        .requestMatchers("/api/projects/**").authenticated()
                        .requestMatchers("/api/advisories/**").authenticated()
                        .requestMatchers("/api/portfolios/**").authenticated()
                        .requestMatchers("/api/files/**").authenticated()

                        // Cualquier otra petición no especificada debe ser autenticada
                        .anyRequest().authenticated())

                // Interceptar peticiones con el filtro JWT antes del filtro de usuario/password
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
