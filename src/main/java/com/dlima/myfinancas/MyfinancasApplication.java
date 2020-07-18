package com.dlima.myfinancas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MyfinancasApplication implements WebMvcConfigurer {
	
	public static void main(String[] args) {
		SpringApplication.run(MyfinancasApplication.class, args);
	}

}
