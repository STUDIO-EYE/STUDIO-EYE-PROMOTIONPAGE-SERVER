package com.example.promotionpage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)

public class PromotionPageApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromotionPageApplication.class, args);
	}

}
