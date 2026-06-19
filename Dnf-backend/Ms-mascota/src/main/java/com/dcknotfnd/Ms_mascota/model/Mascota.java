package com.dcknotfnd.Ms_mascota.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // ej: "dog", "cat", "bird", "other"

    private String breed;
    private String color;
    private String gender; // ej: "Macho", "Hembra"

    @Column(nullable = false)
    private String status; // "lost" o "found"

    private String location; // Nombre del lugar, ej: "Parque Central"

    // Coordenadas separadas para facilitar búsquedas y el mapa
    private Double latitude;
    private Double longitude;

    @Column(length = 1000)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String image;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "created_at")
    private LocalDateTime createdAt; // Para calcular el "timeAgo" en el frontend

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    // Ejecuta esto automáticamente justo antes de guardar en la base de datos
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Mascota() {
    }

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}