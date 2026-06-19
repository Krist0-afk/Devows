package com.dcknotfnd.ms_coincidencias;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.dcknotfnd.ms_coincidencias.repository.CoincidenciaRepository;

@SpringBootTest(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
    "eureka.client.enabled=false"
})
class MsCoincidenciasApplicationTests {

    @MockBean
    private CoincidenciaRepository coincidenciaRepository;

	@Test
	void contextLoads() {
	}

}