package com.notificaciones.Ms_notificaciones.controller;

import com.notificaciones.Ms_notificaciones.dto.AlertaMatchDTO;
import com.notificaciones.Ms_notificaciones.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/enviar-alerta")
    public ResponseEntity<String> enviarNotificacion(@RequestBody AlertaMatchDTO alerta) {
        String asunto = "¡Buenas noticias! Posible coincidencia para " + alerta.getNombreMascota();

        /*
        aqui se envia una notificacion de al usuario, al momento de hacer una coincidencia
        llega una alerta al usuario con el nombre de la mascota y el mensaje de alerta 

        @param alerta: Objeto que contiene la información de la alerta a enviar (correo del dueño, nombre de la mascota, mensaje de alerta)
        @param asunto: Asunto del correo a enviar, que incluye el nombre de la mascota para personalizar la notificación
        @return: Respuesta indicando si la notificación se envió con éxito o si hubo
        */
        
        try {
            emailService.enviarCorreo(alerta.getCorreoDueno(), asunto, alerta.getMensaje());
            return ResponseEntity.ok("Notificación enviada con éxito.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al enviar el correo: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> obtenerNotificaciones() {
        // Retornamos lista vacía o notificaciones estáticas por ahora
        List<Map<String, Object>> notificaciones = new ArrayList<>();
        Map<String, Object> notif = new HashMap<>();
        notif.put("id", 1);
        notif.put("title", "Posible coincidencia");
        notif.put("message", "Alguien ha publicado un perro que se parece a Max.");
        notif.put("date", "Hace 2 horas");
        notif.put("read", false);
        notif.put("type", "match");
        notificaciones.add(notif);
        return ResponseEntity.ok(notificaciones);
    }

    @PostMapping("/read-all")
    public ResponseEntity<Map<String, String>> marcarTodasComoLeidas() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notificaciones marcadas como leídas");
        return ResponseEntity.ok(response);
    }
}