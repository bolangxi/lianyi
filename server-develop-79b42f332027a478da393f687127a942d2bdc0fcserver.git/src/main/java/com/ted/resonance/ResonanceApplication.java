package com.ted.resonance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
@Async
//@EnableJsonFilter
public class ResonanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResonanceApplication.class, args);
	}

}
