package com.lexisware.portafolio.advisory.entities;

import com.lexisware.portafolio.users.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

// Entidad Asesoría - Representa solicitudes de asesoría técnica
@Entity
@Table(name = "advisories")
@Data
public class AdvisoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToOne con User (programador)
    // insertable=false, updatable=false porque ya mapeamos la columna programmer_id
    // abajo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programmer_id", insertable = false, updatable = false)
    private UserEntity programmer;

    // Información del programador que dará la asesoría
    @Column(name = "programmer_id", nullable = false)
    private String programmerId; // UID del programador

    @Column(name = "programmer_email")
    private String programmerEmail;

    @Column(name = "programmer_name")
    private String programmerName;

    // Información del solicitante
    @Column(name = "requester_name", nullable = false)
    private String requesterName;

    @Column(name = "requester_email", nullable = false)
    private String requesterEmail;

    // Detalles de la cita
    private String date; // Fecha de la asesoría
    private String time; // Hora de la asesoría

    @Column(length = 1000)
    private String note; // Nota o descripción de la asesoría

    @Enumerated(EnumType.STRING)
    private Status status = Status.pending; // Estado de la solicitud

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Estados de la asesoría
    public enum Status {
        pending, // Pendiente de aprobación
        approved, // Aprobada
        rejected // Rechazada
    }
}
