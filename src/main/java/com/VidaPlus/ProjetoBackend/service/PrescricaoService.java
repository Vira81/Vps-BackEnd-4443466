package com.VidaPlus.ProjetoBackend.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.PrescricaoDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.PrescricaoEntity;
import com.VidaPlus.ProjetoBackend.entity.ProntuarioEntity;
import com.VidaPlus.ProjetoBackend.repository.PrescricaoRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PrescricaoService {
	@Autowired
	private PrescricaoRepository prescricaoRepository;

	public void gerarPrescricao(ConsultaEntity consulta, PrescricaoDto dto)  {
	    PrescricaoEntity prescricao = new PrescricaoEntity();
	    prescricao.setConsulta(consulta);
	    prescricao.setHospitalNome(consulta.getHospital().getNome());
	    prescricao.setPaciente(consulta.getPaciente());
	    prescricao.setProfissional(consulta.getProfissional());
	    prescricao.setMedicacao(dto.getMedicacao());
	    prescricao.setPosologia(dto.getPosologia());
	    prescricao.setDataCriacao(LocalDateTime.now());

	    prescricaoRepository.save(prescricao);
	}
	
	public byte[] gerarPdfProntuario(PrescricaoEntity prescricao) {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();

	    Document document = new Document();
	    PdfWriter.getInstance(document, out);
	    document.open();

	    document.add(new Paragraph("PRESCRIÇÃO"));
	    document.add(new Paragraph("Hospital: " + prescricao.getConsulta().getHospital().getNome()));
	    document.add(new Paragraph("Paciente: " + prescricao.getPaciente().getNome()));
	    document.add(new Paragraph("Medico: " + prescricao.getProfissional().getPessoa().getNome()));
	    document.add(new Paragraph("Data: " + prescricao.getDataCriacao()));
	    document.add(new Paragraph("Diagnostico: " + prescricao.getConsulta().getProntuario().getDiagnostico()));
	    document.add(new Paragraph("Medicação: " + prescricao.getMedicacao()));
	    document.add(new Paragraph("Posologia: " + prescricao.getPosologia()));

	    document.close();
	    return out.toByteArray();
	}
}
