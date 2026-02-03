package com.lexisware.portafolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// Aplicación principal del backend LEXISWARE Portafolio - @author: LEXIS-TEAM - @version: 1.0.0
@SpringBootApplication
@EnableScheduling
public class PortafolioBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortafolioBackendApplication.class, args);
    }
}
