package com.twoeSystems.aviation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class MetarServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetarServiceApplication.class, args);
	}
	
	

}
