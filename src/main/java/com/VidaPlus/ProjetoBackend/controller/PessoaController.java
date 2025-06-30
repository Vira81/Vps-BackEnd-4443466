package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.PessoaNotNullDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioSaidaDto;
import com.VidaPlus.ProjetoBackend.service.PessoaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/minha_conta")
@CrossOrigin
public class PessoaController {

	@Autowired
	private PessoaService pessoaService;

	/**
	 * Atualiza os dados pessoais do usuario
	 * 
	 * PUT http://localhost:8080/minha_conta
	 * { "nome": "Mario Mario", "cpf": "444.456.444-33", 
	 * "telefone": "(44)99999-0000", "dataNascimento": "1985-09-13" }
	 * 
	 */
	@PutMapping
	public ResponseEntity<String> atualizarPessoa(@Valid @RequestBody PessoaNotNullDto dto) {

		pessoaService.atualizarPessoa(dto);
		return ResponseEntity.ok("Dados Atualizados com sucesso!");
	}
	
	@GetMapping
	public ResponseEntity<UsuarioSaidaDto> minhaConta() {
		UsuarioSaidaDto usuario = pessoaService.minhaConta();
		return ResponseEntity.ok(usuario);
	}
}
