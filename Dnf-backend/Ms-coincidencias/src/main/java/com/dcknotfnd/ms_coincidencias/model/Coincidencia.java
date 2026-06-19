package com.dcknotfnd.ms_coincidencias.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "coincidencias")
public class Coincidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Debe especificar la especie que está buscando")
    private String especieBuscada;

    @NotBlank(message = "Debe especificar el color que está buscando")
    private String colorBuscado;

    private String estado;

    public Coincidencia() {
    }

    public Coincidencia(Long id, String especieBuscada, String colorBuscado, String estado) {
        this.id = id;
        this.especieBuscada = especieBuscada;
        this.colorBuscado = colorBuscado;
        this.estado = estado;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEspecieBuscada() {
        return especieBuscada;
    }

    public void setEspecieBuscada(String especieBuscada) {
        this.especieBuscada = especieBuscada;
    }

    public String getColorBuscado() {
        return colorBuscado;
    }

    public void setColorBuscado(String colorBuscado) {
        this.colorBuscado = colorBuscado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}