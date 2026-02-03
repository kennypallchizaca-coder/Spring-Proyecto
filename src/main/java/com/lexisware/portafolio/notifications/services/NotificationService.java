package com.lexisware.portafolio.notifications.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

    // Inyección opcional de JavaMailSender
    // private final JavaMailSender emailSender;

    public NotificationService() {
        // this.emailSender = emailSender;
    }

    @Scheduled(cron = "0 0 9 * * ?") // Ejecutar todos los días a las 9 AM
    public void sendDailyReminders() {
        log.info("[CRON] Ejecutando recordatorios diarios de asesorías...");
        // Lógica simulada: Buscar asesorías de hoy y mañana
        // advisoryRepository.findByDateBetween(...)

        log.info("[CRON] Recordatorios enviados con éxito.");
    }

    // Método para enviar correos (integración con Spring Mail existente)
    public void sendEmail(String to, String subject, String body) {
        log.info("[EMAIL MOCK] Enviando correo a {} | Asunto: {}", to, subject);
        // if (emailSender != null) { ... }
    }
}
