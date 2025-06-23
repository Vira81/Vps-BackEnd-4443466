package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.HistoricoPacienteDto;
import com.VidaPlus.ProjetoBackend.service.HistoricoPacienteService;

@RestController
@RequestMapping("/historico_clinico")
public class HistoricoPacienteController {

	@Autowired
	private HistoricoPacienteService historicoPacienteService;

	/**
	 * Mostra o historico do paciente
	 * Consultas, exames, cirurgia... (quando implementado)
	 * 
	 * Gera um pdf simples
	 * 
	 * GET http://localhost:8080/historico_clinico/visualizar/
	 * 
	 * GET http://localhost:8080/historico-clinico/visualizar/pdf
	 * 
	 */
	@GetMapping("/visualizar")
	public ResponseEntity<HistoricoPacienteDto> verMeuHistorico() {
		HistoricoPacienteDto dto = historicoPacienteService.buscarHistoricoPaciente();
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/visualizar/pdf")
	public ResponseEntity<byte[]> downloadHistoricoPdf() {
		HistoricoPacienteDto historico = historicoPacienteService.buscarHistoricoPaciente();

		byte[] pdfBytes = historicoPacienteService.gerarPdfHistoricoPaciente(historico);

		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=historico_paciente.pdf")
				.contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
	}

}