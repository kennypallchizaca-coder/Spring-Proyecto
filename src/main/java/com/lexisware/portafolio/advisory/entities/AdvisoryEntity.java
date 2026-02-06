package com.lexisware.portafolio.advisory.entities;

import com.lexisware.portafolio.users.entities.UserEntity;
import jakarta.persistence.*;
import lombok.ToString;
import java.time.LocalDateTime;

// Entidad JPA que representa una solicitud de asesoría técnica
@Entity
@Table(name = "advisories")
public class AdvisoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public AdvisoryEntity() {
    }

    public AdvisoryEntity(Long id, UserEntity programmer, String programmerId, String programmerEmail,
            String programmerName, String requesterName, String requesterEmail, String date, String time, String note,
            Status status, LocalDateTime createdAt) {
        this.id = id;
        this.programmer = programmer;
        this.programmerId = programmerId;
        this.programmerEmail = programmerEmail;
        this.programmerName = programmerName;
        this.requesterName = requesterName;
        this.requesterEmail = requesterEmail;
        this.date = date;
        this.time = time;
        this.note = note;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getProgrammer() {
        return programmer;
    }

    public void setProgrammer(UserEntity programmer) {
        this.programmer = programmer;
    }

    public String getProgrammerId() {
        return programmerId;
    }

    public void setProgrammerId(String programmerId) {
        this.programmerId = programmerId;
    }

    public String getProgrammerEmail() {
        return programmerEmail;
    }

    public void setProgrammerEmail(String programmerEmail) {
        this.programmerEmail = programmerEmail;
    }

    public String getProgrammerName() {
        return programmerName;
    }

    public void setProgrammerName(String programmerName) {
        this.programmerName = programmerName;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AdvisoryEntity that = (AdvisoryEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AdvisoryEntity{" +
                "id=" + id +
                ", programmerId='" + programmerId + '\'' +
                ", programmerEmail='" + programmerEmail + '\'' +
                ", programmerName='" + programmerName + '\'' +
                ", requesterName='" + requesterName + '\'' +
                ", requesterEmail='" + requesterEmail + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", note='" + note + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
