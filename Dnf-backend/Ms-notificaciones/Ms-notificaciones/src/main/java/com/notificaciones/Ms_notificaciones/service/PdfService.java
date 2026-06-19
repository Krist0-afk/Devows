package com.notificaciones.Ms_notificaciones.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.notificaciones.Ms_notificaciones.dto.MascotaPerdidaDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generarCartelBusqueda(MascotaPerdidaDTO mascota) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título Impactante
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 36, BaseColor.RED);
            Paragraph title = new Paragraph("¡SE BUSCA!", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // Nombre de la Mascota
            Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
            Paragraph name = new Paragraph("Nombre: " + mascota.getNombreMascota(), nameFont);
            name.setAlignment(Element.ALIGN_CENTER);
            document.add(name);

            document.add(new Paragraph("\n"));

            // Detalles
            Font detailFont = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
            document.add(new Paragraph("Tipo: " + mascota.getTipo(), detailFont));
            document.add(new Paragraph("Se perdió en: " + mascota.getLugarPerdida(), detailFont));
            document.add(new Paragraph("Descripción: " + mascota.getDescripcion(), detailFont));

            document.add(new Paragraph("\n--------------------------------------------------\n"));

            // Contacto
            Paragraph contactTitle = new Paragraph("INFORMACIÓN DE CONTACTO", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            document.add(contactTitle);
            document.add(new Paragraph("Persona de contacto: " + mascota.getContactoNombre()));
            document.add(new Paragraph("Teléfono: " + mascota.getContactoTelefono()));
            document.add(new Paragraph("Email: " + mascota.getContactoCorreo()));

            document.add(new Paragraph("\n\n"));
            Paragraph footer = new Paragraph("Si la has visto, por favor contáctanos de inmediato.", detailFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}
