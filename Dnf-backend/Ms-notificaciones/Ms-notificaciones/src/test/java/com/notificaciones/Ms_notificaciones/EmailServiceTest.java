package com.notificaciones.Ms_notificaciones;

import com.notificaciones.Ms_notificaciones.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEnviarCorreo() {
        String destinatario = "dueño@test.com";
        String asunto = "¡Buenas noticias!";
        String cuerpo = "Hemos encontrado una posible coincidencia";

        emailService.enviarCorreo(destinatario, asunto, cuerpo);

        verify(mailSender, Mockito.times(1)).send(any(SimpleMailMessage.class));
    }
}
