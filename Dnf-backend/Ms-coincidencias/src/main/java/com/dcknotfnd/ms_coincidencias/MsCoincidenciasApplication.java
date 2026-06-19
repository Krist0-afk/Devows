package com.dcknotfnd.ms_coincidencias;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableCaching
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class MsCoincidenciasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsCoincidenciasApplication.class, args);
    }
}