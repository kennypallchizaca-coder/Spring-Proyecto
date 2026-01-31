package com.lexisware.portafolio.advisory.models;

import lombok.Data;
import java.time.LocalDateTime;
import com.lexisware.portafolio.users.models.User;

// Modelo de Dominio de Asesoría
@Data
public class Advisory {
    private Long id;
    private User programmer;
    private String programmerId;
    private String programmerEmail;
    private String programmerName;
    private String requesterName;
    private String requesterEmail;
    private String date;
    private String time;
    private String note;
    private Status status;
    private LocalDateTime createdAt;

    public enum Status {
        pending,
        approved,
        rejected
    }
}
