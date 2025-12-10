package com.prestek.people;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"CLERK_ISSUER=https://test-issuer.clerk.accounts.dev",
		"CLERK_JWKS_URL=https://test-issuer.clerk.accounts.dev/.well-known/jwks.json",
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"ALLOWED_ORIGINS_HTTP=http://localhost:3000",
		"ALLOWED_ORIGINS_HTTPS=https://localhost:3000"
})
class PeopleServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
