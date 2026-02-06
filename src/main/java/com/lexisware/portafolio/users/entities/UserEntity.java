package com.lexisware.portafolio.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.project.entities.ProjectEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Entidad Usuario - Representa programadores, administradores y usuarios externos
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private String uid; // ID único del usuario

    @Column(nullable = false, unique = true)
    private String email;

    // Password hasheado con BCrypt
    @Column(nullable = false)
    private String password;

    @Column(name = "display_name")
    private String displayName;

    @Enumerated(EnumType.STRING)
    private Role role; // PROGRAMMER, ADMIN, EXTERNAL

    private String specialty; // Especialidad del programador

    @Column(length = 1000)
    private String bio; // Biografía

    private String photoURL; // URL de foto de perfil

    private Boolean available = false; // Disponible para asesorías

    // Redes sociales
    private String github; // URL de GitHub
    private String instagram; // URL de Instagram
    private String whatsapp; // URL de WhatsApp

    // Habilidades técnicas del usuario
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "skill")
    private List<String> skills;

    // Horarios disponibles para asesorías
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_schedules", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "time_slot")
    private List<String> schedule;

    // Relación OneToMany con Projects (proyectos que pertenecen al usuario)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("owner")
    private List<ProjectEntity> projects = new ArrayList<>();

    // Relación OneToMany con Advisories (asesorías donde el usuario es programador)
    @OneToMany(mappedBy = "programmer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("programmer")
    private List<AdvisoryEntity> advisoriesAsProgrammer = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // roles de usuario
    public enum Role {
        PROGRAMMER, // programador que puede recibir asesorías
        ADMIN, // administrador del sistema
        EXTERNAL, // usuario externo que solicita asesorías
    }

    public UserEntity() {
    }

    public UserEntity(String uid, String email, String password, String displayName, Role role, String specialty,
            String bio, String photoURL, Boolean available, String github, String instagram, String whatsapp,
            List<String> skills, List<String> schedule, List<ProjectEntity> projects,
            List<AdvisoryEntity> advisoriesAsProgrammer, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.role = role;
        this.specialty = specialty;
        this.bio = bio;
        this.photoURL = photoURL;
        this.available = available;
        this.github = github;
        this.instagram = instagram;
        this.whatsapp = whatsapp;
        this.skills = skills;
        this.schedule = schedule;
        this.projects = projects;
        this.advisoriesAsProgrammer = advisoriesAsProgrammer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<String> schedule) {
        this.schedule = schedule;
    }

    public List<ProjectEntity> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectEntity> projects) {
        this.projects = projects;
    }

    public List<AdvisoryEntity> getAdvisoriesAsProgrammer() {
        return advisoriesAsProgrammer;
    }

    public void setAdvisoriesAsProgrammer(List<AdvisoryEntity> advisoriesAsProgrammer) {
        this.advisoriesAsProgrammer = advisoriesAsProgrammer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserEntity that = (UserEntity) o;
        return uid != null && uid.equals(that.uid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", role=" + role +
                ", specialty='" + specialty + '\'' +
                ", bio='" + bio + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", available=" + available +
                ", github='" + github + '\'' +
                ", instagram='" + instagram + '\'' +
                ", whatsapp='" + whatsapp + '\'' +
                ", skills=" + skills +
                ", schedule=" + schedule +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
