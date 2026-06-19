package com.comunidad.Ms_comunidad;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MsComunidadApplicationTests {

	@Test
	void testConstructor() {
		// Esto cubre el constructor por defecto de la clase principal para JaCoCo
		MsComunidadApplication app = new MsComunidadApplication();
		assertNotNull(app);
	}

	@Test
	void testMain() {
		// Interceptamos el método estático SpringApplication.run(...)
		try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {

			// Le decimos a Mockito que no haga NADA cuando se intente levantar la app
			mocked.when(() -> SpringApplication.run(MsComunidadApplication.class, new String[]{}))
					.thenReturn(null);

			// Llamamos al método main. JaCoCo lo marcará como "ejecutado" (color verde).
			MsComunidadApplication.main(new String[]{});

			// Verificamos que efectivamente el main() intentó llamar a SpringApplication.run
			mocked.verify(() -> SpringApplication.run(MsComunidadApplication.class, new String[]{}));
		}
	}
}