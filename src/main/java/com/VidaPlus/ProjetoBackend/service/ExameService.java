package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.ExamePacienteDto;
import com.VidaPlus.ProjetoBackend.dto.ExameProfDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.ExameEntity;
import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;
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
	 * 
	 */
	@Transactional
	public ExameEntity criarExamePaciente(ExamePacienteDto dto) {

		PessoaEntity paciente = existe.paciente(dto.getPacienteId());
		HospitalEntity hospital = existe.hospital(dto.getHospitalId());

		existe.dataPassada(dto.getDia());

		// Salva o exame
		ExameEntity exame = new ExameEntity();
		exame.setHospital(hospital);
		exame.setPaciente(paciente);
		exame.setTipoExame(dto.getTipoExame());
		exame.setDia(dto.getDia());
		exame.setDataCriacaoExame(LocalDateTime.now());
		exame.setStatusExame(ConsultaStatus.AGENDADA);

		return exameRepository.save(exame);
	}

	/**
	 * O profissional solicita exames após a consulta
	 */
	@Transactional
	public ExameEntity criarExameAposConsulta(ExameProfDto dto) {

		
		// Medico logado
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();
		ProfissionalSaudeEntity profissional = existe.profissionalSaudeUsuario(usuario.getId());

		// Consulta associada com o exame
		ConsultaEntity consulta = existe.consulta(dto.getConsultaId());
		PessoaEntity paciente = existe.paciente(consulta.getPaciente().getId());
		HospitalEntity hospital = existe.hospital(dto.getHospitalId());

		if (consulta.getStatusConsulta() != ConsultaStatus.REALIZADA) {
			throw new IllegalStateException("Esta consulta não está disponível para exame no momento.");
		}
		existe.dataPassada(dto.getDia());

		// Salva o exame
		ExameEntity exame = new ExameEntity();
		exame.setHospital(hospital);
		exame.setPaciente(paciente);
		exame.setConsulta(consulta);
		exame.setProfissional(profissional);
		exame.setDia(dto.getDia());
		exame.setDataCriacaoExame(LocalDateTime.now());
		exame.setStatusExame(ConsultaStatus.AGENDADA);
		exame.setTipoExame(dto.getTipoExame());

		return exameRepository.save(exame);
	}
	public void fazerExame() {
		
	}
	
}
