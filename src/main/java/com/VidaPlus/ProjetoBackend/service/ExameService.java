package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.ConsultaDto;
import com.VidaPlus.ProjetoBackend.dto.ExameDto;
import com.VidaPlus.ProjetoBackend.dto.ExameGetDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.ExameEntity;
import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.ProntuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.repository.ConsultaRepository;
import com.VidaPlus.ProjetoBackend.repository.ExameRepository;

import jakarta.transaction.Transactional;

@Service
public class ExameService {
	@Autowired
	private ExameRepository exameRepository;

	@Autowired
	private ProntuarioService prontuarioService;

	@Autowired
	private HistoricoPacienteService historicoPacienteService;

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	@Autowired
	private ExisteService existe;

	/**
	 * Cria um exame com Status Agendada Verifica se os Ids informados são adequados
	 * TODO: Melhorar as exceções
	 */
	@Transactional
	public ExameEntity criarExame(ExameDto dto) {

		// Validações
		if (dto.getProfissionalId() != null) {
			ProfissionalSaudeEntity profissional = existe.profissionalSaude(dto.getProfissionalId());
			existe.perfilProfissional(profissional.getUsuario().getPerfil());
		}

		PessoaEntity paciente = existe.paciente(dto.getPacienteId());
		HospitalEntity hospital = existe.hospital(dto.getHospitalId());

		existe.dataPassada(dto.getDia());

		// Salva o exame
		ExameEntity exame = new ExameEntity();
		exame.setHospital(hospital);
		exame.setPaciente(paciente);
		if (dto.getProfissionalId() != null) {
			exame.setProfissional(existe.profissionalSaude(dto.getProfissionalId()));
		}
		exame.setDia(dto.getDia());
		exame.setDataCriacaoExame(LocalDateTime.now());
		exame.setStatusExame(ConsultaStatus.AGENDADA);

		return exameRepository.save(exame);
	}
}
