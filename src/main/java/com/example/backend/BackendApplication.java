package com.example.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing() // Esta es la clave - ignora si no encuentra el archivo
				.load();

		System.setProperty("MYSQLUSER", dotenv.get("MYSQLUSER"));
		System.setProperty("MYSQLPASSWORD", dotenv.get("MYSQLPASSWORD"));
		System.setProperty("CLIENT_ID", dotenv.get("CLIENT_ID"));
		System.setProperty("SECRET", dotenv.get("SECRET"));
		System.setProperty("EMAIL", dotenv.get("EMAIL"));
		System.setProperty("PASSWORD", dotenv.get("PASSWORD"));

		SpringApplication.run(BackendApplication.class, args);
	}
}
