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

import com.VidaPlus.ProjetoBackend.entity.PrescricaoEntity;
import com.VidaPlus.ProjetoBackend.entity.ProntuarioEntity;
import com.VidaPlus.ProjetoBackend.exception.EmailJaCadastradoException;
import com.VidaPlus.ProjetoBackend.repository.PrescricaoRepository;
import com.VidaPlus.ProjetoBackend.repository.ProntuarioRepository;
import com.VidaPlus.ProjetoBackend.service.PrescricaoService;
import com.VidaPlus.ProjetoBackend.service.ProntuarioService;

//TODO: Implementar Get normal
@RestController
@RequestMapping("/prescricao")
@CrossOrigin
public class PrescricaoController {
	
	
	@Autowired
	private PrescricaoRepository prescricaoRepository;
	
	@Autowired
	private PrescricaoService prescricaoService;
	
	@GetMapping("/{id}/pdf")
	public ResponseEntity<byte[]> gerarPdf(@PathVariable Long id) {
		PrescricaoEntity prontuario = prescricaoRepository.findById(id)
				.orElseThrow(() -> new EmailJaCadastradoException("Prescrição não encontrado"));

		byte[] pdfBytes = prescricaoService.gerarPdfProntuario(prontuario);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prescricao_" + id + ".pdf")
				.contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
	}
}
