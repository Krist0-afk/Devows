package com.comunidad.Ms_comunidad.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HistoriaTest {

    @Test
    void testGettersYSetters() {
        Historia historia = new Historia();
        historia.setId(1L);
        historia.setTitle("Final Feliz");
        historia.setFamily("Familia Baez");
        historia.setLocation("Puerto Montt");
        historia.setContent("La mascota se adaptó perfectamente al nuevo hogar.");
        historia.setImage("imagen.png");
        historia.setTimeAgo("2 días");

        assertEquals(1L, historia.getId());
        assertEquals("Final Feliz", historia.getTitle());
        assertEquals("Familia Baez", historia.getFamily());
        assertEquals("Puerto Montt", historia.getLocation());
        assertEquals("La mascota se adaptó perfectamente al nuevo hogar.", historia.getContent());
        assertEquals("imagen.png", historia.getImage());
        assertEquals("2 días", historia.getTimeAgo());
    }

    @Test
    void testConstructorVacio() {
        Historia historia = new Historia();
        // Verificamos que se instancie correctamente
        assertNull(historia.getId());
    }
}