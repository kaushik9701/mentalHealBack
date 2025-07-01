package com.mentalHeal.mentalHeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MentalHealApplication {
	public static void main(String[] args) {
		SpringApplication.run(MentalHealApplication.class, args);
	}
}
