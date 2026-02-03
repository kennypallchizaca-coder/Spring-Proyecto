package com.lexisware.portafolio.notifications.services;

import com.lexisware.portafolio.advisory.repositories.AdvisoryRepository;
import com.lexisware.portafolio.utils.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final AdvisoryRepository advisoryRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * ?") // Ejecutar todos los días a las 9 AM
    public void sendDailyReminders() {
        log.info("[CRON] Ejecutando recordatorios diarios de asesorías...");

        // Buscar asesorías para el día siguiente (mañana)
        java.time.LocalDate tomorrowDate = java.time.LocalDate.now().plusDays(1);
        String tomorrow = tomorrowDate.toString(); // LocalDate.toString() devuelve formato ISO (YYYY-MM-DD)
        java.util.List<com.lexisware.portafolio.advisory.entities.AdvisoryEntity> advisories = advisoryRepository
                .findByDate(tomorrow);

        for (com.lexisware.portafolio.advisory.entities.AdvisoryEntity advisory : advisories) {
            if (advisory.getStatus() == com.lexisware.portafolio.advisory.entities.AdvisoryEntity.Status.approved) {
                // Enviar recordatorio al programador
                try {
                    emailService.sendHtmlEmail(
                            advisory.getProgrammerEmail(),
                            "Recordatorio: Asesoría Mañana",
                            "Hola " + advisory.getProgrammerName() + ", tienes una asesoría programada para mañana con "
                                    + advisory.getRequesterName() + " a las " + advisory.getTime());
                    log.info("Recordatorio enviado a programador: {}", advisory.getProgrammerEmail());
                } catch (Exception e) {
                    log.error("Error enviando recordatorio a programador: {}", e.getMessage());
                }

                // Enviar recordatorio al solicitante
                try {
                    emailService.sendHtmlEmail(
                            advisory.getRequesterEmail(),
                            "Recordatorio: Asesoría Mañana",
                            "Hola " + advisory.getRequesterName() + ", tienes una asesoría programada para mañana con "
                                    + advisory.getProgrammerName() + " a las " + advisory.getTime());
                    log.info("Recordatorio enviado a solicitante: {}", advisory.getRequesterEmail());
                } catch (Exception e) {
                    log.error("Error enviando recordatorio a solicitante: {}", e.getMessage());
                }
            }
        }

        log.info("[CRON] Recordatorios enviados con éxito. Total procesados: {}", advisories.size());
    }

    // Método para enviar correos (integración con Spring Mail existente)
    public void sendEmail(String to, String subject, String body) {
        emailService.sendHtmlEmail(to, subject, body);
    }
}
