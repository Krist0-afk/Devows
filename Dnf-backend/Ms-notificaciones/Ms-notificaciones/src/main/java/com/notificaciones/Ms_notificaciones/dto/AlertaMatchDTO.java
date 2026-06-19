package com.notificaciones.Ms_notificaciones.dto;

public class AlertaMatchDTO {
    private String correoDueno;
    private String nombreMascota;
    private String mensaje;

    public AlertaMatchDTO() {}

    public String getCorreoDueno() { return correoDueno; }
    public void setCorreoDueno(String correoDueno) { this.correoDueno = correoDueno; }
    public String getNombreMascota() { return nombreMascota; }
    public void setNombreMascota(String nombreMascota) { this.nombreMascota = nombreMascota; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}   