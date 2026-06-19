package com.usuario.Ms_usuario;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class MsUsuarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUsuarioApplication.class, args);
    }

}