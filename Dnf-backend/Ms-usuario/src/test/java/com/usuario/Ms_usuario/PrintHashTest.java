package com.usuario.Ms_usuario;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PrintHashTest {
    @Test
    public void printHash() {
        System.out.println("HASH_IS: " + new BCryptPasswordEncoder().encode("Admin123!"));
    }
}
