package com.keylent.space;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpaceIngestionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpaceIngestionServiceApplication.class, args);
	}

}
