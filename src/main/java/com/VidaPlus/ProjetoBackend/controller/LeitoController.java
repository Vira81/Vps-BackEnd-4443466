package com.VidaPlus.ProjetoBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.LeitoGetDto;
import com.VidaPlus.ProjetoBackend.service.LeitoService;

@RestController
@RequestMapping("/leito")
@CrossOrigin
public class LeitoController {
	@Autowired
	private LeitoService leitoService;
	
	/**
	 * Busca todos os leitos
	 * 
	 * ADMIN GET http://localhost:8080/leito
	 * 
	 */
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<LeitoGetDto>> listarLeitos() {
		List<LeitoGetDto> leito = leitoService.buscarTodosLeitos();
		return ResponseEntity.ok(leito);
	}
	
	/**
	 * Busca todos os leitos por hospital
	 * 
	 * ADMIN GET http://localhost:8080/leito/1
	 * 
	 */
	@GetMapping("/{hospitalId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<LeitoGetDto>> listarLeitosHospital(@PathVariable Long hospitalId) {
		List<LeitoGetDto> leito = leitoService.buscarLeitosHospital(hospitalId);
		return ResponseEntity.ok(leito);
	}
	
}
