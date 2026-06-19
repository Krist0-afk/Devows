package com.comunidad.Ms_comunidad.model;

import jakarta.persistence.*;

@Entity
@Table(name = "historias")
public class Historia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String family;
    private String location;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(length = 1000)
    private String image;
    
    private String timeAgo;

    public Historia() {
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getTimeAgo() { return timeAgo; }
    public void setTimeAgo(String timeAgo) { this.timeAgo = timeAgo; }
}