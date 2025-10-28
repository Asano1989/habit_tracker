package com.example.learningtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LearningTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningTrackerApplication.class, args);
	}

}
