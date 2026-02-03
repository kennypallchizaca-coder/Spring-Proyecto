package com.lexisware.portafolio.reports.services;

import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.repositories.UserRepository;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.advisory.repositories.AdvisoryRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import com.lexisware.portafolio.project.repositories.ProjectRepository;
import com.lexisware.portafolio.project.entities.ProjectEntity;

@Service
public class ReportService {

    private final UserRepository userRepository;
    private final AdvisoryRepository advisoryRepository;
    private final ProjectRepository projectRepository;

    public ReportService(UserRepository userRepository, AdvisoryRepository advisoryRepository,
            ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.advisoryRepository = advisoryRepository;
        this.projectRepository = projectRepository;
    }

    public ByteArrayInputStream generateUserProjectsPdf(String userUid) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Paragraph title = new Paragraph("Reporte de Proyectos", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Info del Usuario
            UserEntity user = userRepository.findById(userUid).orElse(null);
            String userName = (user != null) ? user.getDisplayName() : "Usuario Desconocido";
            Paragraph subtitle = new Paragraph("Programador: " + userName);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);

            document.add(new Paragraph(" ")); // Espacio

            // Tabla
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 3, 4, 3 });

            addTableHeader(table, "Título");
            addTableHeader(table, "Descripción");
            addTableHeader(table, "Tecnologías");

            List<ProjectEntity> projects = projectRepository
                    .findByOwner_Uid(userUid, org.springframework.data.domain.Pageable.unpaged()).getContent();

            for (ProjectEntity project : projects) {
                table.addCell(project.getTitle());
                table.addCell(project.getDescription() != null ? project.getDescription() : "Sin descripción");

                String stack = (project.getTechStack() != null) ? String.join(", ", project.getTechStack()) : "N/A";
                table.addCell(stack);
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generateProgrammersPdf() {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Paragraph title = new Paragraph("Reporte de Programadores - LEXISWARE", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Espacio

            // Tabla
            PdfPTable table = new PdfPTable(4); // 4 columnas
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 3, 3, 3, 2 });

            // Encabezados
            addTableHeader(table, "Nombre");
            addTableHeader(table, "Email");
            addTableHeader(table, "Especialidad");
            addTableHeader(table, "Rol");

            // Datos
            List<UserEntity> programmers = userRepository.findAll(); // En producción filtrar por rol si es necesario
            for (UserEntity user : programmers) {
                if (user.getRole() == UserEntity.Role.PROGRAMMER) {
                    table.addCell(user.getDisplayName() != null ? user.getDisplayName() : "N/A");
                    table.addCell(user.getEmail());
                    table.addCell(user.getSpecialty() != null ? user.getSpecialty() : "N/A");
                    table.addCell(user.getRole().toString());
                }
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generateAdvisoriesPdf() {
        Document document = new Document(PageSize.A4.rotate()); // Horizontal para más columnas
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Paragraph title = new Paragraph("Reporte de Asesorías - LEXISWARE", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Tabla
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 3, 3, 2, 2, 2 });

            addTableHeader(table, "Solicitante");
            addTableHeader(table, "Programador");
            addTableHeader(table, "Fecha");
            addTableHeader(table, "Hora");
            addTableHeader(table, "Estado");

            List<AdvisoryEntity> advisories = advisoryRepository.findAll();
            for (AdvisoryEntity adv : advisories) {
                table.addCell(adv.getRequesterName() != null ? adv.getRequesterName() : "Anonimo");
                table.addCell(adv.getProgrammer() != null ? adv.getProgrammer().getDisplayName() : "N/A");
                table.addCell(adv.getDate() != null ? adv.getDate().toString() : "N/A");
                table.addCell(adv.getTime() != null ? adv.getTime() : "N/A");

                PdfPCell statusCell = new PdfPCell(new Phrase(adv.getStatus().toString()));
                // Colorear según estado
                if (AdvisoryEntity.Status.approved == adv.getStatus())
                    statusCell.setBackgroundColor(Color.GREEN);
                else if (AdvisoryEntity.Status.rejected == adv.getStatus())
                    statusCell.setBackgroundColor(Color.RED);
                else
                    statusCell.setBackgroundColor(Color.YELLOW);

                table.addCell(statusCell);
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(Color.LIGHT_GRAY);
        header.setBorderWidth(2);
        header.setPhrase(new Phrase(headerTitle));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header);
    }
}
