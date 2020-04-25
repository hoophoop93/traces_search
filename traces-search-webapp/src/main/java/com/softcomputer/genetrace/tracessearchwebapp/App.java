package com.softcomputer.genetrace.tracessearchwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.softcomputer.genetrace.ReadFromFile;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

	}

	@Bean
	@Scope("session") 
	public ReadFromFile readFromFile() {
		return new ReadFromFile();
	}

}
