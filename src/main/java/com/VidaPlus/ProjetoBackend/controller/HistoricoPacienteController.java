package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.HistoricoPacienteDto;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.repository.HistoricoPacienteRepository;
import com.VidaPlus.ProjetoBackend.service.HistoricoPacienteService;
import com.VidaPlus.ProjetoBackend.service.UsuarioLogadoService;

@RestController
@RequestMapping("/historico-clinico")
@PreAuthorize("hasRole('PACIENTE')")
public class HistoricoPacienteController {

	@Autowired
	private HistoricoPacienteService historicoPacienteService;
	
	@Autowired
	private HistoricoPacienteRepository historicoPacienteRepository;
	
	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	@GetMapping("/visualizar")
	public ResponseEntity<HistoricoPacienteDto> verMeuHistorico() {
		HistoricoPacienteDto dto = historicoPacienteService.buscarHistoricoPaciente();
		return ResponseEntity.ok(dto);
	}
	@GetMapping("/visualizar/pdf")
	@PreAuthorize("hasRole('PACIENTE')")
	public ResponseEntity<byte[]> downloadHistoricoPdf() {
	    UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();
	    //PessoaEntity pessoa = usuario.getPessoa();
	    HistoricoPacienteDto historico = historicoPacienteService.buscarHistoricoPaciente();

	    byte[] pdfBytes = historicoPacienteService.gerarPdfHistoricoPaciente(historico);

	    return ResponseEntity.ok()
	            .header("Content-Disposition", "attachment; filename=historico_paciente.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);
	}

}