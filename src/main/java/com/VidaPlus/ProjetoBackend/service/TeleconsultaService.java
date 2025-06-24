package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.ProntuarioDto;
import com.VidaPlus.ProjetoBackend.dto.TeleconsultaDto;
import com.VidaPlus.ProjetoBackend.dto.TeleconsultaSaidaDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.TeleconsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusTeleconsulta;
import com.VidaPlus.ProjetoBackend.repository.ConsultaRepository;
import com.VidaPlus.ProjetoBackend.repository.TeleconsultaRepository;

@Service
public class TeleconsultaService {

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private TeleconsultaRepository teleconsultaRepository;

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	@Autowired
	private ProntuarioService prontuarioService;

	@Autowired
	private HistoricoPacienteService historicoPacienteService;

	@Autowired
	private ExisteService existe;

	/**
	 * Criação da teleconsulta
	 * TODO: Alterar Dto para informar a hora
	 */
	public TeleconsultaSaidaDto agendarTeleconsulta(TeleconsultaDto dto) {

		// Paciente solicitando telemedicina e profissional solocitado
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();
		ProfissionalSaudeEntity profissional = existe.profissionalSaude(dto.getProfissionalId());
		HospitalEntity hospitalVirtual = existe.hospitalVirtual(4L);

		// Consulta comum
		ConsultaEntity consulta = new ConsultaEntity();
		consulta.setDia(dto.getDia());
		// placeholder
		consulta.setHora(LocalTime.now());
		consulta.setHospital(hospitalVirtual);
		consulta.setPaciente(usuario.getPessoa());
		consulta.setProfissional(profissional);
		consulta.setStatusConsulta(ConsultaStatus.AGENDADA);
		consulta.setTeleconsulta(true);

		consultaRepository.save(consulta);

		// Teleconsulta
		TeleconsultaEntity teleconsulta = new TeleconsultaEntity();
		teleconsulta.setConsulta(consulta);
		teleconsulta.setStatus(StatusTeleconsulta.AGENDADA);

		// link usado na consulta
		String sala = UUID.randomUUID().toString().replace("-", "");
		String link = "http://localhost:8080/teleconsulta/sala/" + sala;
		teleconsulta.setLinkConsulta(link);
		teleconsulta.setSalaId(sala);
		teleconsulta.setDataGeracaoLink(LocalDateTime.now());

		teleconsultaRepository.save(teleconsulta);

		return saida(teleconsulta);
	}

	private TeleconsultaSaidaDto saida(TeleconsultaEntity tele) {
		return new TeleconsultaSaidaDto(tele.getId(), tele.getConsulta().getId(), tele.getLinkConsulta(),
				tele.getStatus(), tele.getDataGeracaoLink());
	}

	/**
	 * Paciente entra na teleconsulta
	 */
	public TeleconsultaSaidaDto realizarTeleconsulta(String salaId) {

		// Identificação
		TeleconsultaEntity teleconsulta = existe.teleconsulta(salaId);
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();

		// Status
		if (teleconsulta.getStatus() != StatusTeleconsulta.AGENDADA) {
			throw new IllegalStateException("A teleconsulta já foi iniciada ou encerrada.");
		}

		// Paciente é dono da consulta
		if (!teleconsulta.getConsulta().getPaciente().getId().equals(usuario.getId())) {
			throw new IllegalStateException("Como vc chegou aqui???. Verifique o Token.");
		}

		// Marca como em andamento
		teleconsulta.setStatus(StatusTeleconsulta.ANDAMENTO);
		teleconsultaRepository.save(teleconsulta);

		return saida(teleconsulta);
	}

	/**
	 * Profissional finaliza a teleconsulta, igual na consulta normal
	 */
	public TeleconsultaSaidaDto realizarTeleconsultaProf(String salaId, ProntuarioDto dto) {
		TeleconsultaEntity teleconsulta = existe.teleconsulta(salaId);
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();

		// Status
		if (teleconsulta.getStatus() != StatusTeleconsulta.ANDAMENTO) {
			throw new IllegalStateException("A teleconsulta ainda não foi iniciada ou já encerrou.");
		}

		// Profissional é dono da consulta
		if (!teleconsulta.getConsulta().getProfissional().getPessoa().getId().equals(usuario.getId())) {
			throw new IllegalStateException("Como vc chegou aqui???. Verifique o Token.");
		}

		// Atualiza a consulta
		teleconsulta.setStatus(StatusTeleconsulta.FINALIZADA);
		teleconsulta.getConsulta().setStatusConsulta(ConsultaStatus.REALIZADA);
		teleconsulta.getConsulta().setDataRealizada(LocalDateTime.now());

		// Gera o prontuario
		prontuarioService.gerarProntuario(teleconsulta.getConsulta(), dto);

		// Salva no histórico do paciente
		String descricao = String.format("TeleConsulta realizada em %s com Dr(a). %s.",
				teleconsulta.getConsulta().getDataRealizada().toLocalDate(),
				teleconsulta.getConsulta().getProfissional().getPessoa().getNome());

		historicoPacienteService.registrarEntradaConsulta(teleconsulta.getConsulta().getPaciente(),
				teleconsulta.getConsulta().getProfissional(), descricao, teleconsulta.getConsulta());

		return saida(teleconsulta);

	}
}