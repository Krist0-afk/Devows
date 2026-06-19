package com.usuario.Ms_usuario;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MsUsuarioApplicationTests {

    @Test
    void testConstructor() {
        // Esto cubre el constructor por defecto de la clase para JaCoCo
        MsUsuarioApplication app = new MsUsuarioApplication();
        assertNotNull(app);
    }

    @Test
    void testMain() {
        // Interceptamos el método estático SpringApplication.run(...)
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {

            // Le decimos a Mockito que no haga NADA cuando se intente levantar la app
            mocked.when(() -> SpringApplication.run(MsUsuarioApplication.class, new String[]{}))
                    .thenReturn(null);

            // Llamamos a tu método main. JaCoCo lo marcará como "ejecutado" (color verde).
            MsUsuarioApplication.main(new String[]{});

            // Verificamos que efectivamente el main() intentó llamar a SpringApplication.run
            mocked.verify(() -> SpringApplication.run(MsUsuarioApplication.class, new String[]{}));
        }
    }
}