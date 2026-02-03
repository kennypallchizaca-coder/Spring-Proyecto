package com.lexisware.portafolio.dashboard.dtos;

import lombok.Data;

@Data
public class UserGrowthStats {
    private Integer month; // 1-12
    private Integer year;
    private Long count;

    public UserGrowthStats(Integer month, Integer year, Long count) {
        this.month = month;
        this.year = year;
        this.count = count;
    }
}
