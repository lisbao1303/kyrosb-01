package com.kyros.CadastroAPI;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.kyros.CadastroAPI")
@EntityScan(basePackages = "com.kyros.CadastroAPI.models")
public class CadastroApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(CadastroApiApplication.class, args);
	}
}
