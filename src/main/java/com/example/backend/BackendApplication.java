package com.example.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		System.setProperty("MYSQLUSER", dotenv.get("MYSQLUSER"));
		System.setProperty("MYSQLPASSWORD", dotenv.get("MYSQLPASSWORD"));
		SpringApplication.run(BackendApplication.class, args);
	}

}
