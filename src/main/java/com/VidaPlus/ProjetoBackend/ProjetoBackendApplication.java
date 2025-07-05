package com.VidaPlus.ProjetoBackend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SuppressWarnings("unused")
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
public class ProjetoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoBackendApplication.class, args);
		
		// Senha Hash para inserir via SQL
		//String novaHash = new BCryptPasswordEncoder().encode("admin");
	    //System.out.println(novaHash);
	   
	}

}
