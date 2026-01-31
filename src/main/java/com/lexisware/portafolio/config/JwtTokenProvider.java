package com.lexisware.portafolio.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

//proveedor de tokens jwt - genera y valida tokens para autenticación
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // obtiene la clave de firma
    private SecretKey obtenerClaveFirma() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // genera un token jwt para un usuario
    public String generarToken(String userId, String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(userId)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(obtenerClaveFirma())
                .compact();
    }

    // extrae el uid del usuario desde el token
    public String obtenerIdUsuarioDeToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // extrae el email del usuario desde el token
    public String obtenerEmailDeToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("email", String.class);
    }

    // extrae el rol del usuario desde el token
    public String obtenerRolDeToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("role", String.class);
    }

    // valida un token jwt
    public boolean validarToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(obtenerClaveFirma())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SecurityException ex) {
            log.error("Firma JWT inválida");
        } catch (MalformedJwtException ex) {
            log.error("Token JWT inválido");
        } catch (ExpiredJwtException ex) {
            log.error("Token JWT expirado");
        } catch (UnsupportedJwtException ex) {
            log.error("Token JWT no soportado");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims vacío");
        }
        return false;
    }
}
