package com.lexisware.portafolio.dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProjectCount {
    private String userName;
    private long projectCount;
}
