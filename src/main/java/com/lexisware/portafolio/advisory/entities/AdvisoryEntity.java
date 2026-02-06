package com.lexisware.portafolio.advisory.entities;

import com.lexisware.portafolio.users.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

// Entidad JPA que representa una solicitud de asesoría técnica
@Entity
@Table(name = "advisories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdvisoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // Usuario que actúa como programador/mentor en la sesión
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programmer_id", insertable = false, updatable = false)
    @ToString.Exclude
    private UserEntity programmer;

    // UID del programador asignado
    @Column(name = "programmer_id", nullable = false)
    private String programmerId;

    @Column(name = "programmer_email")
    private String programmerEmail;

    @Column(name = "programmer_name")
    private String programmerName;

    // Nombre del usuario que solicita la asesoría
    @Column(name = "requester_name", nullable = false)
    private String requesterName;

    @Column(name = "requester_email", nullable = false)
    private String requesterEmail;

    // Detalles de agenda de la sesión
    private String date; // Fecha programada para la asesoría
    private String time; // Hora pactada para el encuentro

    @Column(length = 1000)
    private String note; // Nota o descripción de la asesoría

    @Enumerated(EnumType.STRING)
    private Status status = Status.pending; // Estado de la solicitud

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Enumeración de los estados posibles de una solicitud
    public enum Status {
        pending, // Esperando respuesta del programador
        approved, // Aceptada y programada
        rejected // Denegada por el mentor
    }
}
