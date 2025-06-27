package com.VidaPlus.ProjetoBackend.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.ExameDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.service.ExameService;
import com.VidaPlus.ProjetoBackend.service.ExisteService;
import com.VidaPlus.ProjetoBackend.service.UsuarioLogadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/exame")
@CrossOrigin
public class ExameController {
	@Autowired
	private UsuarioLogadoService usuarioLogadoService;
	
	@Autowired
	private ExameService exameService;
	
	@Autowired
	private ExisteService existe;
	
	/**
	 * Cria um exame para o usuario logado
	 * 
	 * POST http://localhost:8080/exame/novo_exame 
	 * { "profissionalId": 1,
	 * "hospitalId": 1, "dia": "2025-06-23" }
	 */

	@PostMapping("/novo_exame")
	public ResponseEntity<?> criarExameComoPaciente(@Valid @RequestBody ExameDto dto) {
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();

		dto.setPacienteId(usuario.getId());

		exameService.criarExame(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Exame criado com sucesso!");
	}
	
	/**
	 * Profissional de saude solicita um exame apos a consulta
	 * 
	 * POST http://localhost:8080/consulta/nova_exame_consulta 
	 * { "pacienteId": 1,
	 * "hospitalId": 1, "dia": "2025-06-23" }
	 */
	@PreAuthorize("hasRole('PROFISSIONAL')")
	@PostMapping("/novo_exame_consulta/{consultaId}")
	public ResponseEntity<?> criarExameComoProfissional(@Valid @PathVariable Long consultaId, @RequestBody ExameDto dto) throws AccessDeniedException {
		// Medico logado
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();
		ProfissionalSaudeEntity profissional = existe.profissionalSaudeUsuario(usuario.getId());
		
		// Consulta associada com o exame
		ConsultaEntity consulta = existe.consulta(consultaId);
		
		dto.setProfissionalId(profissional.getId());
		dto.setPacienteId(consulta.getPaciente().getId());
		dto.setConsultaId(consultaId);
		
		exameService.criarExame(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Exame criado com sucesso!");
	}


}
