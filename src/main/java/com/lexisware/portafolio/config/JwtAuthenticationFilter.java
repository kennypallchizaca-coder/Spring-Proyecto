package com.lexisware.portafolio.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

// Filtro de interceptación para validar tokens JWT en cada petición HTTP
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extraer el token de la cabecera "Authorization: Bearer <token>"
            String jwt = obtenerJwtDeRequest(request);

            // Validar existencia y formato del token
            if (StringUtils.hasText(jwt) && tokenProvider.validarToken(jwt)) {
                String userId = tokenProvider.getUserIdFromJWT(jwt);
                String userRole = tokenProvider.getUserRoleFromJWT(jwt);
                String userEmail = tokenProvider.getUserEmailFromJWT(jwt);

                // Crear objeto de autoridad basado en el rol del usuario
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole);

                // Establecer el contexto de seguridad de Spring con la información del usuario
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, userEmail, Collections.singletonList(authority));

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("No se pudo establecer la autenticación del usuario en el contexto de seguridad", ex);
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Utilidad para extraer el token JWT de la cabecera de la petición
    private String obtenerJwtDeRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
