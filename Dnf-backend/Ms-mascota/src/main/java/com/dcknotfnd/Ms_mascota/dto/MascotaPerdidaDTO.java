package com.dcknotfnd.Ms_mascota.dto;

import java.io.Serializable;

public class MascotaPerdidaDTO implements Serializable {
    private String nombreMascota;
    private String tipo;
    private String descripcion;
    private String lugarPerdida;
    private String contactoNombre;
    private String contactoCorreo;
    private String contactoTelefono;

    public MascotaPerdidaDTO() {
    }

    public MascotaPerdidaDTO(String nombreMascota, String tipo, String descripcion, String lugarPerdida, String contactoNombre, String contactoCorreo, String contactoTelefono) {
        this.nombreMascota = nombreMascota;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.lugarPerdida = lugarPerdida;
        this.contactoNombre = contactoNombre;
        this.contactoCorreo = contactoCorreo;
        this.contactoTelefono = contactoTelefono;
    }

    public String getNombreMascota() { return nombreMascota; }
    public void setNombreMascota(String nombreMascota) { this.nombreMascota = nombreMascota; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getLugarPerdida() { return lugarPerdida; }
    public void setLugarPerdida(String lugarPerdida) { this.lugarPerdida = lugarPerdida; }

    public String getContactoNombre() { return contactoNombre; }
    public void setContactoNombre(String contactoNombre) { this.contactoNombre = contactoNombre; }

    public String getContactoCorreo() { return contactoCorreo; }
    public void setContactoCorreo(String contactoCorreo) { this.contactoCorreo = contactoCorreo; }

    public String getContactoTelefono() { return contactoTelefono; }
    public void setContactoTelefono(String contactoTelefono) { this.contactoTelefono = contactoTelefono; }
}
