package de.samply.teiler.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"de.samply"})
public class TeilerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeilerBackendApplication.class, args);
	}

}
