package com.dcknotfnd.Ms_mascota;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("No DB available in CI/CD or local test without docker-compose")
class MsMascotaApplicationTests {

	@Test
	void contextLoads() {
	}

}
