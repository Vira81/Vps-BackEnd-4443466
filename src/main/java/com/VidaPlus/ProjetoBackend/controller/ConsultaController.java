package com.VidaPlus.ProjetoBackend.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.ConsultaDto;
import com.VidaPlus.ProjetoBackend.dto.ConsultaGetDto;
import com.VidaPlus.ProjetoBackend.dto.PrescricaoDto;
import com.VidaPlus.ProjetoBackend.dto.RealizarConsultaDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.repository.ProfissionalSaudeRepository;
import com.VidaPlus.ProjetoBackend.service.ConsultaService;
import com.VidaPlus.ProjetoBackend.service.PrescricaoService;
import com.VidaPlus.ProjetoBackend.service.UsuarioLogadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/consulta")
@CrossOrigin
public class ConsultaController {
	/**
	 * 
	 */
	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	@Autowired
	private ConsultaService consultaService;

	@Autowired
	private PrescricaoService prescricaoService;
	
	@Autowired
	private ProfissionalSaudeRepository profissionalSaudeRepository;

	/**
	 * Somente o admin pode ver todas as consultas TODO: criar um GET exclusivo para
	 * o profissional e o paciente
	 * 
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<ConsultaGetDto> buscarPorId(@PathVariable Long id) {
	    ConsultaEntity consulta = consultaService.buscarPorId(id);
	    ConsultaGetDto responseDto = new ConsultaGetDto(consulta);
	    return ResponseEntity.ok(responseDto);
	}

	/**
	 * Cria uma consulta para o usuario logado
	 */

	@PostMapping("/nova_consulta")
	public ResponseEntity<?> criarConsultaComoPaciente(@RequestBody ConsultaDto dto) {
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();

		dto.setPacienteId(usuario.getId());

		consultaService.criarConsulta(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Consulta criada com sucesso!");
	}
	
	//cancelar consulta
	@PutMapping("/{id}/cancelar")
	@PreAuthorize("hasRole('PACIENTE')")
	public ResponseEntity<?> cancelarConsulta(@PathVariable Long id) {
	    consultaService.cancelarConsultaPaciente(id);
	    return ResponseEntity.ok("Consulta cancelada com sucesso.");
	}
	

	// Profissional de saude cria uma consulta com ele e o paciente
	@PreAuthorize("hasRole('PROFISSIONAL')")
	@PostMapping("/nova_consulta_profissional")
	public ResponseEntity<?> criarConsultaComoProfissional(@RequestBody ConsultaDto dto) throws AccessDeniedException {
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();

		if (!usuario.getPerfil().equals(PerfilUsuario.PROFISSIONAL)) {
			throw new AccessDeniedException("Apenas profissionais podem acessar este recurso.");
		}

		ProfissionalSaudeEntity profissional = profissionalSaudeRepository.findByUsuarioId(usuario.getId())
				.orElseThrow(() -> new RuntimeException("Profissional não encontrado."));

		dto.setProfissionalId(profissional.getId());

		consultaService.criarConsulta(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Consulta criada com sucesso!");
	}

	// Admin cria consulta livremente
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/nova_consulta_admin")
	public ResponseEntity<?> criarConsultaComoAdmin(@RequestBody ConsultaDto dto) {
		consultaService.criarConsulta(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Consulta criada com sucesso!");
	}

	/**
	 * Atendimento da consulta
	 */
	@PostMapping("/{consultaId}/atender")
	@PreAuthorize("hasRole('PROFISSIONAL')")
	public ResponseEntity<?> atenderConsulta(@PathVariable Long consultaId,
			@RequestBody @Valid RealizarConsultaDto dto) {
		consultaService.realizarConsulta(consultaId, dto);
		return ResponseEntity.ok("Consulta realizada.");
	}

	
	/**
	 * Prescricao
	 */
	@PostMapping("/{consultaId}/atender/prescricao")
	@PreAuthorize("hasRole('PROFISSIONAL')")
	public ResponseEntity<?> atenderConsultaPrescicao(@PathVariable Long consultaId,
            @RequestBody @Valid PrescricaoDto dto){
		ConsultaEntity consulta = consultaService.buscarPorId(consultaId); 
	    prescricaoService.gerarPrescricao(consulta, dto);
		return ResponseEntity.ok("Prescricao gerada.");
	}

}
