package de.samply.teiler.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"de.samply"})
public class TeilerCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeilerCoreApplication.class, args);
	}

}
