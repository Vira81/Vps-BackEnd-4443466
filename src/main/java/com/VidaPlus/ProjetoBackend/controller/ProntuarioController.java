package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.entity.ProntuarioEntity;
import com.VidaPlus.ProjetoBackend.exception.EmailJaCadastradoException;
import com.VidaPlus.ProjetoBackend.repository.ProntuarioRepository;
import com.VidaPlus.ProjetoBackend.service.ProntuarioService;

//TODO: Implementar Get normal
@RestController
@RequestMapping("/prontuario")
@CrossOrigin
public class ProntuarioController {
	
	
	@Autowired
	private ProntuarioRepository prontuarioRepository;
	
	@Autowired
	private ProntuarioService prontuarioService;
	
	@GetMapping("/{id}/pdf")
	public ResponseEntity<byte[]> gerarPdf(@PathVariable Long id) {
		ProntuarioEntity prontuario = prontuarioRepository.findById(id)
				.orElseThrow(() -> new EmailJaCadastradoException("Prontuário não encontrado"));

		byte[] pdfBytes = prontuarioService.gerarPdfProntuario(prontuario);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prontuario_" + id + ".pdf")
				.contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
	}
}
