package com.VidaPlus.ProjetoBackend.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.VidaPlus.ProjetoBackend.dto.ProntuarioDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.repository.ProfissionalSaudeRepository;
import com.VidaPlus.ProjetoBackend.service.ConsultaService;
import com.VidaPlus.ProjetoBackend.service.PrescricaoService;
import com.VidaPlus.ProjetoBackend.service.UsuarioLogadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/consulta")
@CrossOrigin
public class ConsultaController {

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	@Autowired
	private ConsultaService consultaService;

	@Autowired
	private PrescricaoService prescricaoService;

	@Autowired
	private ProfissionalSaudeRepository profissionalSaudeRepository;

	/**
	 * Busca consulta por Id
	 * 
	 * ADMIN GET http://localhost:8080/consulta/1
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
	 * Busca todas as consultas
	 * 
	 * ADMIN GET http://localhost:8080/consulta
	 * 
	 */
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ConsultaGetDto>> listarTodas() {
		List<ConsultaGetDto> consultas = consultaService.buscarTodasConsultas();
		return ResponseEntity.ok(consultas);
	}

	/**
	 * 2 buscas para as consultas do usuario logado
	 * 
	 * Todas GET http://localhost:8080/consulta/minhas
	 * 
	 * Somente as marcadas como agendada GET
	 * http://localhost:8080/consulta/minhas/agendadas
	 */
	/**
	 * Agora a lista é retornada em paginas
	 * 
	 * pagina 1
	 * GET http://localhost:8080/consulta/minhas
	 * 
	 * outras paginas
	 * http://localhost:8080/consulta/minhas?page=2
	 */
	@GetMapping("/minhas")
	public Page<ConsultaGetDto> listarConsultasPaciente(
	        @PageableDefault(size = 10, sort = "dia") Pageable pageable) {
	    return consultaService.buscarConsultasDoUsuario(pageable);
	}


	@GetMapping("/minhas/agendadas")
	public ResponseEntity<List<ConsultaGetDto>> listarConsultasAgendadas() {
		return ResponseEntity.ok(consultaService.buscarConsultasAgendadasDoUsuario());
	}

	/**
	 * Lista todas as consultas do profissional logado, marcada para hoje
	 * 
	 * GET http://localhost:8080/consulta/hoje
	 */
	@PreAuthorize("hasRole('PROFISSIONAL')")
	@GetMapping("/hoje")
	public ResponseEntity<List<ConsultaGetDto>> listarConsultasProfissionalHoje() {
		return ResponseEntity.ok(consultaService.buscarConsultasProfissionalHoje());
	}

	/**
	 * Cria uma consulta para o usuario logado
	 * 
	 * POST http://localhost:8080/consulta/nova_consulta 
	 * { "profissionalId": 1,
	 * "hospitalId": 1, "dia": "2025-06-23", "hora": "14:07:00", "valor": 1300.0 }
	 */

	@PostMapping("/nova_consulta")
	public ResponseEntity<?> criarConsultaComoPaciente(@RequestBody ConsultaDto dto) {
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();

		dto.setPacienteId(usuario.getId());

		consultaService.criarConsulta(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Consulta criada com sucesso!");
	}

	/**
	 * Cancela a consulta, marcando como CANCELADA_PACIENTE
	 * 
	 * PUT http://localhost:8080/consulta/1/cancelar
	 */
	@PutMapping("/{id}/cancelar")
	@PreAuthorize("hasRole('PACIENTE')")
	public ResponseEntity<?> cancelarConsulta(@PathVariable Long id) {
		consultaService.cancelarConsultaPaciente(id);
		return ResponseEntity.ok("Consulta cancelada com sucesso.");
	}

	/**
	 * Profissional de saude cria uma consulta com ele e um paciente
	 * 
	 * POST http://localhost:8080/consulta/nova_consulta_profissional 
	 * {
	 * "pacienteId": 3, "hospitalId": 1, "dia": "2025-06-23", "hora": "14:07:00",
	 * "valor": 1300.0 }
	 */
	@PreAuthorize("hasRole('PROFISSIONAL')")
	@PostMapping("/nova_consulta_profissional")
	public ResponseEntity<?> criarConsultaComoProfissional(@RequestBody ConsultaDto dto) throws AccessDeniedException {
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();

		ProfissionalSaudeEntity profissional = profissionalSaudeRepository.findByUsuarioId(usuario.getId())
				.orElseThrow(() -> new RuntimeException("Profissional não encontrado."));

		dto.setProfissionalId(profissional.getId());

		consultaService.criarConsulta(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Consulta criada com sucesso!");
	}

	/**
	 * Admin cria a consulta com um profissional e um paciente
	 * 
	 * POST http://localhost:8080/consulta/nova_consulta_profissional 
	 * {"profissionalId": 1,
	 * "pacienteId": 3, "hospitalId": 1, "dia": "2025-06-23", "hora": "14:07:00",
	 * "valor": 1300.0 }
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/nova_consulta_admin")
	public ResponseEntity<?> criarConsultaComoAdmin(@RequestBody ConsultaDto dto) {
		consultaService.criarConsulta(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Consulta criada com sucesso!");
	}

	/**
	 * Atendimento da consulta , é necessario enviar dados para a 
	 * Criação do prontuario
	 * 
	 * Profissional
	 * POST http://localhost:8080/consulta/1/atender
	 * { "diagnostico": "Dolor",
	 * "observacao": "sit amet"}
	 */
	@PostMapping("/{consultaId}/atender")
	@PreAuthorize("hasRole('PROFISSIONAL')")
	public ResponseEntity<?> atenderConsulta(@Valid @PathVariable Long consultaId,
			@RequestBody @Valid ProntuarioDto dto) {
		consultaService.realizarConsulta(consultaId, dto);
		return ResponseEntity.ok("Consulta realizada.");
	}

	/**
	 * Prescricao , não é obrigatória para a consulta, historico, etc
	 * 
	 * Profissional
	 * POST http://localhost:8080/consulta/1/atender/prescricao
	 * {  "medicacao": "Ipsum,",  "posologia": "consectetur"}
	 */
	@PostMapping("/{consultaId}/atender/prescricao")
	@PreAuthorize("hasRole('PROFISSIONAL')")
	public ResponseEntity<?> atenderConsultaPrescicao(@PathVariable Long consultaId,
			@RequestBody @Valid PrescricaoDto dto) {
		ConsultaEntity consulta = consultaService.buscarPorId(consultaId);
		prescricaoService.gerarPrescricao(consulta, dto);
		return ResponseEntity.ok("Prescricao gerada.");
	}

}
