package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.AuthenticationDto;

import com.VidaPlus.ProjetoBackend.service.AuthService;



@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

	@Autowired
	private AuthService authService;
	
	/**
	 * Pagina de Login
	 * POST http://localhost:8080/auth/login
	 * 
	 * É necessario usar usename , password no JSON
	 * TODO: Atualizar ultimo Acesso
	 * 
	 */
	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationDto authDto){
		return ResponseEntity.ok(authService.login(authDto));
	}
	
	
	
	
	
}