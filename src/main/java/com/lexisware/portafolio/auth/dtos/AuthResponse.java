package com.lexisware.portafolio.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.models.User;

// DTO para respuesta de autenticación
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token; // JWT token
    private UserDTO user;

    // DTO simplificado del usuario (sin password)
    @Data
    @AllArgsConstructor
    public static class UserDTO {
        private String uid;
        private String email;
        private String displayName;
        private String role;
        private Boolean available;

        // Convierte User entity a DTO
        public static UserDTO fromEntity(UserEntity user) {
            return new UserDTO(
                    user.getUid(),
                    user.getEmail(),
                    user.getDisplayName(),
                    user.getRole() != null ? user.getRole().name() : null,
                    user.getAvailable());
        }

        // Convierte User model a DTO
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
