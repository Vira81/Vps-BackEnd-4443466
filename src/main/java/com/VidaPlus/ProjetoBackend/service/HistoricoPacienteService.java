package com.VidaPlus.ProjetoBackend.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.HistoricoPacienteDto;
import com.VidaPlus.ProjetoBackend.dto.NovoHistoricoPacienteDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.HistoricoPacienteEntity;
import com.VidaPlus.ProjetoBackend.entity.NovoHistoricoPacienteEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.PrescricaoEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.ProntuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.TipoEspecialidadeSaude;
import com.VidaPlus.ProjetoBackend.repository.HistoricoPacienteRepository;
import com.VidaPlus.ProjetoBackend.repository.PessoaRepository;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class HistoricoPacienteService {
	@Autowired
	private HistoricoPacienteRepository historicoPacienteRepository;

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	@Autowired
	private PessoaRepository pessoaRepository;

	/**
	 * Atualiza o histórico do paciente no momento que uma consulta é marcada como
	 * realizada
	 */
	public void registrarEntradaConsulta(PessoaEntity paciente, ProfissionalSaudeEntity profissional, String descricao,
			ConsultaEntity consulta) {
		// Cria o historico se não existe ainda
		HistoricoPacienteEntity historico = historicoPacienteRepository.findByPaciente(paciente)
				.orElseGet(() -> criarNovoHistorico(paciente));

		NovoHistoricoPacienteEntity novo = new NovoHistoricoPacienteEntity();
		novo.setDataEntrada(LocalDate.now());
		novo.setDescricao(descricao);
		novo.setProfissionalResponsavel(profissional);
		novo.setHistoricoPaciente(historico);

		if (consulta.getTeleconsulta() == true) {
			novo.setTipo(TipoEspecialidadeSaude.TELECONSULTA);
		} else {
			novo.setTipo(TipoEspecialidadeSaude.CONSULTA);
		}

		historico.getEntradas().add(novo);
		historico.setDataUltimaAtualizacao(LocalDate.now());

		novo.setConsulta(consulta);

		historicoPacienteRepository.save(historico);
	}

	/**
	 * Criação de um novo histórico do paciente
	 */
	private HistoricoPacienteEntity criarNovoHistorico(PessoaEntity paciente) {
		HistoricoPacienteEntity historico = new HistoricoPacienteEntity();
		historico.setPaciente(paciente);
		historico.setDataUltimaAtualizacao(LocalDate.now());
		historico.setEntradas(new ArrayList<>());
		return historico;
	}

	/**
	 * Busca o histórico do paciente logado
	 */
	public HistoricoPacienteDto buscarHistoricoPaciente() {
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();

		PessoaEntity paciente = pessoaRepository.findByUsuario(usuario)
				.orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

		HistoricoPacienteEntity historico = historicoPacienteRepository.findByPaciente(paciente)
				.orElseThrow(() -> new RuntimeException("Histórico clínico não encontrado."));

		return historicoSaida(historico);
	}

	/**
	 * Organiza a informação do histórico Retorna a lista com histórico
	 */
	private HistoricoPacienteDto historicoSaida(HistoricoPacienteEntity entity) {
		List<NovoHistoricoPacienteDto> entradas = entity.getEntradas().stream().map(entrada -> {
			NovoHistoricoPacienteDto dto = new NovoHistoricoPacienteDto();
			dto.setDataEntrada(entrada.getDataEntrada());
			dto.setDescricao(entrada.getDescricao());
			dto.setTipo(entrada.getTipo());
			dto.setNomeProfissional(entrada.getProfissionalResponsavel().getPessoa().getNome());

			// Prontuario , Prescrição existe
			if (entrada.getConsulta() != null && entrada.getConsulta().getProntuario() != null) {
				ProntuarioEntity prontuario = entrada.getConsulta().getProntuario();
				dto.setDiagnostico(prontuario.getDiagnostico());
				dto.setObservacao(prontuario.getObservacao());
				if (entrada.getConsulta().getPrescricao() != null) {
					PrescricaoEntity prescricao = entrada.getConsulta().getPrescricao();
					dto.setMedicacao(prescricao.getMedicacao());
					dto.setPosologia(prescricao.getPosologia());
				}
			}

			return dto;
		}).toList();

		HistoricoPacienteDto dto = new HistoricoPacienteDto();
		dto.setId(entity.getId());
		dto.setDataUltimaAtualizacao(entity.getDataUltimaAtualizacao());
		dto.setEntradas(entradas);
		return dto;
	}

	/**
	 * Criação do PDF do histórico
	 */
	public byte[] gerarPdfHistoricoPaciente(HistoricoPacienteDto historico) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);

		document.open();

		document.add(new Paragraph("Histórico Clínico do Paciente"));
		document.add(new Paragraph("Última atualização: " + historico.getDataUltimaAtualizacao()));
		document.add(Chunk.NEWLINE);

		List<NovoHistoricoPacienteDto> entradas = historico.getEntradas();

		// Imprimi a lista
		for (NovoHistoricoPacienteDto entrada : entradas) {
			document.add(new Paragraph("Data: " + entrada.getDataEntrada()));
			document.add(new Paragraph("Tipo: " + entrada.getTipo()));
			document.add(new Paragraph("Profissional: Dr(a). " + entrada.getNomeProfissional()));
			document.add(new Paragraph("Descrição: " + entrada.getDescricao()));

			if (entrada.getDiagnostico() != null) {
				document.add(new Paragraph("Diagnóstico: " + entrada.getDiagnostico()));
			}
			if (entrada.getObservacao() != null) {
				document.add(new Paragraph("Observações: " + entrada.getObservacao()));
			}
			if (entrada.getMedicacao() != null) {
				document.add(new Paragraph("Medicação: " + entrada.getMedicacao()));
			}
			if (entrada.getPosologia() != null) {
				document.add(new Paragraph("Posologia: " + entrada.getPosologia()));
			}

			document.add(Chunk.NEWLINE);
		}

		document.close();

		return outputStream.toByteArray();
	}
}
