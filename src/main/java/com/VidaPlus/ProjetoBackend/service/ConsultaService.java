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
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
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
	private HistoricoPacienteService historicoPacienteService;

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	/**
	 * Cria uma consulta com Status Agendada
	 * Verifica se os Ids informados são adequados 
	 * TODO: Melhorar as exceções
	 */
	@Transactional
	public ConsultaEntity criarConsulta(ConsultaDto dto) {

		// Profissional existe
		ProfissionalSaudeEntity profissional = profissionalRepository.findById(dto.getProfissionalId())
				.orElseThrow(() -> new RuntimeException("Profissional não encontrado."));

		// Perfil Profissional
		if (profissional.getUsuario().getPerfil() != PerfilUsuario.PROFISSIONAL) {
			throw new RuntimeException("Usuário não é profissional de saude.");
		}

		// Paciente existe
		PessoaEntity paciente = pessoaRepository.findById(dto.getPacienteId())
				.orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

		// Hospital existe
		HospitalEntity hospital = hospitalRepository.findById(dto.getHospitalId())
				.orElseThrow(() -> new RuntimeException("Hospital não existe."));

		// Salva a consulta com Status Agendada
		ConsultaEntity novaConsulta = ConsultaEntity.builder().profissional(profissional).paciente(paciente)
				.hospital(hospital).dia(dto.getDia()).hora(dto.getHora()).statusConsulta(ConsultaStatus.AGENDADA)
				.valor(dto.getValor()).build();

		return consultaRepository.save(novaConsulta);
	}

	/**
	 * Busca uma consulta pelo Id
	 * TODO: Criar mais buscas.
	 */
	public ConsultaEntity buscarPorId(Long id) {
		return consultaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada"));
	}

	/**
	 * Funcionario atende o Paciente.
	 * Marca a consulta como realizada.
	 * Cria o prontuario.
	 * Cria/Atualiza histórico do Paciente
	 * TODO: Melhorar exception
	 **/
	public void realizarConsulta(Long consultaId, RealizarConsultaDto dto) {
		// Consulta existe
		ConsultaEntity consulta = consultaRepository.findById(consultaId)
				.orElseThrow(() -> new EmailJaCadastradoException("Consulta não encontrada."));

		// Identificação do Paciente
		String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();

		// Profissional é responsável pela consulta
		if (!consulta.getProfissional().getUsuario().getEmail().equals(emailLogado)) {
			throw new AccessDeniedException("Você não tem permissão para alterar esta consulta.");
		}

		// Consulta já foi realizada
		if (consulta.getStatusConsulta() == ConsultaStatus.REALIZADA) {
			throw new IllegalStateException("Esta consulta já foi finalizada.");
		}

		// Consulta foi cancelada
		if (!consulta.getStatusConsulta().equals(ConsultaStatus.AGENDADA)) {
			throw new IllegalStateException("Esta consulta foi cancelada: " + consulta.getStatusConsulta());
		}

		// Atualiza a consulta
		consulta.setStatusConsulta(ConsultaStatus.REALIZADA);
		consulta.setDataRealizada(LocalDateTime.now());

		consultaRepository.save(consulta);

		// Gera o prontuario com os dados fornecidos
		prontuarioService.gerarProntuario(consulta, dto);

		// Salva no histórico do paciente
		String descricao = String.format("Consulta realizada em %s com Dr(a). %s.",
				consulta.getDataRealizada().toLocalDate(), consulta.getProfissional().getPessoa().getNome());

		historicoPacienteService.registrarEntradaConsulta(consulta.getPaciente(), consulta.getProfissional(), descricao,
				consulta);
	}

	/**
	 * Paciente pede o cancelamento da consulta
	 * TODO: Aplicar regras para o cancelamento
	 */
	public void cancelarConsultaPaciente(Long consultaId) {
		// Consulta existe
		ConsultaEntity consulta = consultaRepository.findById(consultaId)
				.orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada"));

		// Identificação do Paciente
		UsuarioEntity usuarioLogado = usuarioLogadoService.getUsuarioLogado();

		// Paciente logado é o dono da consulta
		if (!consulta.getPaciente().getUsuario().getId().equals(usuarioLogado.getId())) {
			throw new AccessDeniedException("Você não pode cancelar essa consulta. Verifique Id.");
		}

		// Consulta está Agendada
		if (!consulta.getStatusConsulta().equals(ConsultaStatus.AGENDADA)) {
			throw new IllegalStateException("Apenas consultas agendadas podem ser canceladas.");
		}

		// Marca como cancelada
		consulta.setStatusConsulta(ConsultaStatus.CANCELADA_PACIENTE);
		consultaRepository.save(consulta);

	}
	//TODO: Criar cancelamento pelo profissional e por outro motivo.
}
