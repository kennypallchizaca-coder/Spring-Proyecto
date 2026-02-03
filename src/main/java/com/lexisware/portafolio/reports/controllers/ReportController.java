package com.lexisware.portafolio.reports.controllers;

import com.lexisware.portafolio.reports.services.ReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/programmers/pdf")
    @SuppressWarnings("null")
    public ResponseEntity<InputStreamResource> downloadProgrammersReport() {
        ByteArrayInputStream bis = reportService.generateProgrammersPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=programadores.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/advisories/pdf")
    public ResponseEntity<InputStreamResource> downloadAdvisoriesReport() {
        ByteArrayInputStream bis = reportService.generateAdvisoriesPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=asesorias.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/projects/{uid}/pdf")
    public ResponseEntity<InputStreamResource> downloadUserProjectsReport(
            @org.springframework.web.bind.annotation.PathVariable("uid") String uid) {
        ByteArrayInputStream bis = reportService.generateUserProjectsPdf(uid);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=proyectos.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
