package com.notificaciones.Ms_notificaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(asunto);
        message.setText(cuerpo);
        message.setFrom("no-reply@sanosysalvos.cl");

        mailSender.send(message);
    }

    public void enviarCorreoConAdjunto(String destinatario, String asunto, String cuerpo, byte[] adjunto, String nombreAdjunto) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpo);
            helper.setFrom("no-reply@sanosysalvos.cl");
            helper.addAttachment(nombreAdjunto, new ByteArrayResource(adjunto));

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
/*
aqui se define el cuerpo del correo, el destinatario, el asunto y el remitente del correo, 
para luego enviar el correo al usuario

@param destinatario: Dirección de correo electrónico del destinatario
@param asunto: Asunto del correo
@param cuerpo: Cuerpo del correo

*/