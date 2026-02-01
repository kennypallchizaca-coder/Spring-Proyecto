package com.lexisware.portafolio.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.models.User;

// Respuesta exitosa tras la autenticación, con token JWT y datos del usuario
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token; // Token JWT para autorización en cabeceras HTTP
    private UserDTO user;

    // DTO anidado para transferir datos básicos del usuario sin sensitive info
    @Data
    @AllArgsConstructor
    public static class UserDTO {
        private String uid;
        private String email;
        private String displayName;
        private String role;
        private Boolean available;

        // Factory method para crear el DTO desde una Entidad JPA
        public static UserDTO fromEntity(UserEntity user) {
            return new UserDTO(
                    user.getUid(),
                    user.getEmail(),
                    user.getDisplayName(),
                    user.getRole() != null ? user.getRole().name() : null,
                    user.getAvailable());
        }

        // Factory method para crear el DTO desde un Modelo de negocio
        public static UserDTO fromModel(User user) {
            return new UserDTO(
                    user.getUid(),
                    user.getEmail(),
                    user.getDisplayName(),
                    user.getRole() != null ? user.getRole().name() : null,
                    user.getAvailable());
        }
    }
}
