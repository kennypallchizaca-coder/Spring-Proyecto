package com.lexisware.portafolio.dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvisoryStatsDto {
    private String label; // Could be month/year or programmer name
    private Long count;
}
