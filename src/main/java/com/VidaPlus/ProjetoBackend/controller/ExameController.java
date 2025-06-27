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

import com.VidaPlus.ProjetoBackend.dto.ExameProfDto;
import com.VidaPlus.ProjetoBackend.dto.ExamePacienteDto;
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
	 * POST http://localhost:8080/exame/novo 
	 * {"hospitalId": 1, "dia": "2025-06-23", "tipoExame":"SANGUE" }
	 */

	@PostMapping("/novo")
	public ResponseEntity<?> criarExameComoPaciente(@Valid @RequestBody ExamePacienteDto dto) {
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();
		dto.setPacienteId(usuario.getId());

		exameService.criarExamePaciente(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Exame criado com sucesso!");
	}
	
	/**
	 * Profissional de saude solicita um exame apos a consulta
	 * 
	 * POST http://localhost:8080/exame/1/consulta 
	 * {"hospitalId": 1, "dia": "2025-06-23", "tipoExame":"SANGUE" }
	 */
	@PreAuthorize("hasRole('PROFISSIONAL')")
	@PostMapping("/{consultaId}/consulta")
	public ResponseEntity<?> criarExameComoProfissional(@PathVariable Long consultaId, @Valid @RequestBody ExameProfDto dto) throws AccessDeniedException {
		
		dto.setConsultaId(consultaId);
		
		exameService.criarExameAposConsulta(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Exame criado com sucesso!");
	}


}
