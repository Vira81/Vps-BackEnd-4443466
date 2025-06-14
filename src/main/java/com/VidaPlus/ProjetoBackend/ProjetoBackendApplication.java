package com.VidaPlus.ProjetoBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ProjetoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoBackendApplication.class, args);
	}

}
