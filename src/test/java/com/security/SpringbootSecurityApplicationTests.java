package com.security;

import com.security.entities.UserEntity;
import com.security.services.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootSecurityApplicationTests {

	@Autowired
	private JWTService jwtService;

	@Test
	void contextLoads() {
		UserEntity userEntity = UserEntity.builder()
				.id(2L)
				.email("user@gmail.com")
				.password("243wrw")
				.build();
		String token = jwtService.generateAccessToken(userEntity);
		System.out.println(token);

		Long id = jwtService.getUserIdFromToken(token);
		System.out.println(id);
	}

}
