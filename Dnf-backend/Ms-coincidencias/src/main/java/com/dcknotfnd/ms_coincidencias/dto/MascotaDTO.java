package com.dcknotfnd.ms_coincidencias.dto;

import lombok.Data;

@Data
public class MascotaDTO {
    private Long id;
    private String name;
    private String type;
    private String breed;
    private String gender;
    private String status;
    private String location;
    private Double latitude;
    private Double longitude;
    private String timeAgo;
    private String image;
}