package com.VidaPlus.ProjetoBackend.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.RealizarConsultaDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProntuarioEntity;
import com.VidaPlus.ProjetoBackend.repository.ProntuarioRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ProntuarioService {
	
	@Autowired
	private ProntuarioRepository prontuarioRepository;

	/**
	 * Cria o prontuario
	 * Obrigatório no momento da realização da consulta
	 */
	public void gerarProntuario(ConsultaEntity consulta, RealizarConsultaDto dto) {
	    ProntuarioEntity prontuario = new ProntuarioEntity();
	    prontuario.setConsulta(consulta);
	    prontuario.setPaciente(consulta.getPaciente());
	    prontuario.setProfissional(consulta.getProfissional());
	    prontuario.setDiagnostico(dto.getDiagnostico());
	    prontuario.setObservacao(dto.getObservacao());
	    prontuario.setDataCriacao(LocalDateTime.now());

	    prontuarioRepository.save(prontuario);
	}
	
	/**
	 * Gera o PDF do prontuário
	 * 
	 */
	public byte[] gerarPdfProntuario(ProntuarioEntity prontuario) {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();

	    Document document = new Document();
	    PdfWriter.getInstance(document, out);
	    document.open();

	    document.add(new Paragraph("PRONTUÁRIO"));
	    document.add(new Paragraph("Paciente: " + prontuario.getPaciente().getNome()));
	    document.add(new Paragraph("Medico: " + prontuario.getProfissional().getPessoa().getNome()));
	    document.add(new Paragraph("Data: " + prontuario.getDataCriacao()));
	    document.add(new Paragraph("Diagnostico: " + prontuario.getDiagnostico()));
	    document.add(new Paragraph("Observação: " + prontuario.getObservacao()));

	    document.close();
	    return out.toByteArray();
	}


}
