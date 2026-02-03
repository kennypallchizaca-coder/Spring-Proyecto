package com.lexisware.portafolio.dashboard.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStats {
    private long programmersCount;
    private long projectsCount;
    private long advisoriesPending;
    private long advisoriesApproved;
    private long advisoriesRejected;
}
