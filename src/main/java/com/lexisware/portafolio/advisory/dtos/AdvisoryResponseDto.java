package com.lexisware.portafolio.advisory.dtos;

import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import lombok.Data;
import java.time.LocalDateTime;

// DTO para respuesta de asesoría
@Data
public class AdvisoryResponseDto {

    private Long id;
    private String programmerId;
    private String programmerEmail;
    private String programmerName;
    private String requesterName;
    private String requesterEmail;
    private String date;
    private String time;
    private String note;
    private AdvisoryEntity.Status status;
    private LocalDateTime createdAt;
}
