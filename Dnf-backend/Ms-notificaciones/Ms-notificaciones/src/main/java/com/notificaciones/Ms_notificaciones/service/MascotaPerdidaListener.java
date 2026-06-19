package com.notificaciones.Ms_notificaciones.service;

import com.notificaciones.Ms_notificaciones.dto.MascotaPerdidaDTO;
import com.notificaciones.Ms_notificaciones.security.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MascotaPerdidaListener {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void recibirReporteExtravio(MascotaPerdidaDTO mascota) {
        System.out.println("Reporte de mascota extraviada recibido: " + mascota.getNombreMascota());

        // 1. Generar Cartel (PDF)
        byte[] pdf = pdfService.generarCartelBusqueda(mascota);

        // 2. Enviar Correo al dueño con el cartel adjunto
        emailService.enviarCorreoConAdjunto(
                mascota.getContactoCorreo(),
                "Cartel de Búsqueda: ¡Ayúdanos a encontrar a " + mascota.getNombreMascota() + "!",
                "Hola " + mascota.getContactoNombre() + ",\n\n" +
                "Lamentamos mucho que " + mascota.getNombreMascota() + " se haya perdido. " +
                "Adjuntamos un cartel de búsqueda generado automáticamente para que puedas imprimirlo y compartirlo.",
                pdf,
                "Cartel_Busqueda_" + mascota.getNombreMascota() + ".pdf"
        );

        System.out.println("Cartel enviado a: " + mascota.getContactoCorreo());
    }
}
