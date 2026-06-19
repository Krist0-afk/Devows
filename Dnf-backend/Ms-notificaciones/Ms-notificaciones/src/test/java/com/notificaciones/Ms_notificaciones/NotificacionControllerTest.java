package com.notificaciones.Ms_notificaciones;

import com.notificaciones.Ms_notificaciones.controller.NotificacionController;
import com.notificaciones.Ms_notificaciones.dto.AlertaMatchDTO;
import com.notificaciones.Ms_notificaciones.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

public class NotificacionControllerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificacionController notificacionController;

    private AlertaMatchDTO alerta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        alerta = new AlertaMatchDTO();
        alerta.setCorreoDueno("dueño@test.com");
        alerta.setNombreMascota("Firulais");
        alerta.setMensaje("Hemos encontrado una posible coincidencia");
    }

    @Test
    void enviarNotificacionExito() {
        Mockito.doNothing().when(emailService).enviarCorreo(anyString(), anyString(), anyString());

        ResponseEntity<String> response = notificacionController.enviarNotificacion(alerta);
        
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Notificación enviada con éxito.", response.getBody());
    }

    @Test
    void enviarNotificacionError() {
        Mockito.doThrow(new RuntimeException("Simulated error")).when(emailService).enviarCorreo(anyString(), anyString(), anyString());

        ResponseEntity<String> response = notificacionController.enviarNotificacion(alerta);
        
        assertEquals(500, response.getStatusCode().value());
        assertEquals("Error al enviar el correo: Simulated error", response.getBody());
    }
}
