package com.notificaciones.Ms_notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MascotaPerdidaDTO implements Serializable {
    private String nombreMascota;
    private String tipo;
    private String descripcion;
    private String lugarPerdida;
    private String contactoNombre;
    private String contactoCorreo;
    private String contactoTelefono;
}
