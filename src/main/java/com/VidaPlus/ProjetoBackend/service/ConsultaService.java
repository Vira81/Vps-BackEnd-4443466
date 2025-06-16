package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.ConsultaDto;
import com.VidaPlus.ProjetoBackend.dto.RealizarConsultaDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.exception.EmailJaCadastradoException;
import com.VidaPlus.ProjetoBackend.repository.ConsultaRepository;
import com.VidaPlus.ProjetoBackend.repository.HospitalRepository;
import com.VidaPlus.ProjetoBackend.repository.PessoaRepository;
import com.VidaPlus.ProjetoBackend.repository.ProfissionalSaudeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ConsultaService {

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private ProfissionalSaudeRepository profissionalRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private ProntuarioService prontuarioService;

	@Autowired
	private PrescricaoService prescricaoService;

	@Transactional
	public ConsultaEntity criarConsulta(ConsultaDto dto) {

		ProfissionalSaudeEntity profissional = profissionalRepository.findById(dto.getProfissionalId())
				.orElseThrow(() -> new RuntimeException("Profissional não encontrado."));

		// verifica perfil de profissional
		if (profissional.getUsuario().getPerfil() != PerfilUsuario.PROFISSIONAL) {
			throw new RuntimeException("Usuário não é profissional de saude.");
		}

		PessoaEntity paciente = pessoaRepository.findById(dto.getPacienteId())
				.orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

		HospitalEntity hospital = hospitalRepository.findById(dto.getHospitalId())
				.orElseThrow(() -> new RuntimeException("Hospital não existe."));

		// Cria a consulta
		ConsultaEntity novaConsulta = ConsultaEntity.builder().profissional(profissional).paciente(paciente)
				.hospital(hospital).dia(dto.getDia()).hora(dto.getHora()).statusConsulta(ConsultaStatus.AGENDADA)
				.valor(dto.getValor()).build();

		return consultaRepository.save(novaConsulta);
	}

	public ConsultaEntity buscarPorId(Long id) {
		return consultaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada"));
	}

	// Marca a consulta como realizada, e inicia o prontuario
	// TODO: criar exception
	public void realizarConsulta(Long consultaId, RealizarConsultaDto dto) {
		ConsultaEntity consulta = consultaRepository.findById(consultaId)
				.orElseThrow(() -> new EmailJaCadastradoException("Consulta não encontrada."));

		String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();

		// Verifica se o profissional é o responsável pela consulta
		if (!consulta.getProfissional().getUsuario().getEmail().equals(emailLogado)) {
			throw new AccessDeniedException("Você não tem permissão para alterar esta consulta.");
		}

		// Não pode alterar uma consulta já realizada
		if (consulta.getStatusConsulta() == ConsultaStatus.REALIZADA) {
			throw new IllegalStateException("Esta consulta já foi finalizada.");
		}

		consulta.setStatusConsulta(ConsultaStatus.REALIZADA);
		consulta.setDataRealizada(LocalDateTime.now());

		consultaRepository.save(consulta);

		prontuarioService.gerarProntuario(consulta, dto);
	}
}
