package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.ConsultaDto;
import com.VidaPlus.ProjetoBackend.dto.ConsultaGetDto;
import com.VidaPlus.ProjetoBackend.dto.ProntuarioDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.repository.ConsultaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ConsultaService {

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private ProntuarioService prontuarioService;

	@Autowired
	private HistoricoPacienteService historicoPacienteService;

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	@Autowired
	private ExisteService existe;
	
	/**
	 * Cria uma consulta com Status Agendada
	 * Verifica se os Ids informados são adequados 
	 * TODO: Melhorar as exceções
	 */
	@Transactional
	public ConsultaEntity criarConsulta(ConsultaDto dto) {

		// Valida pelo Id
		ProfissionalSaudeEntity profissional = existe.profissionalSaude(dto.getProfissionalId());
		PessoaEntity paciente = existe.paciente(dto.getPacienteId());
		HospitalEntity hospital = existe.hospital(dto.getHospitalId());
		
		// Perfil Profissional
		if (profissional.getUsuario().getPerfil() != PerfilUsuario.PROFISSIONAL) {
			throw new RuntimeException("Usuário não é profissional de saude.");
		}

		// Medico trabalha no hospital
		if (!profissional.getHospitais().stream()
		        .anyMatch(h -> h.getId().equals(dto.getHospitalId()))) {
		    throw new RuntimeException("Este profissional não atende no hospital informado.");
		}
		
		// Medico trabalha no dia 
		if (!profissional.getDiasTrabalho().contains(dto.getDia().getDayOfWeek()))  {
			throw new RuntimeException("O "+ profissional.getFuncao() + " não está disponível nesse dia. " + dto.getDia().getDayOfWeek());
		}
		
		// Data no passado
		if (dto.getDia().isBefore(LocalDate.now())) {
		    throw new RuntimeException("A data informada já passou.");
		}
		
		
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
	 * Lista todas as consultas
	 * 
	 */
	public List<ConsultaGetDto> buscarTodasConsultas() {
        return consultaRepository.findAll()
                .stream()
                .map(ConsultaGetDto::new) 
                .collect(Collectors.toList());
    }
	
	/**
	 * Lista todas as consultas do usuario logado
	 * 
	 */
	public List<ConsultaGetDto> buscarConsultasDoUsuario() {
		// Identificação
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();
        PessoaEntity pessoa = usuario.getPessoa();

        // Lista com as consultas
            return consultaRepository.findByPacienteId(pessoa.getId())
                    .stream()
                    .map(ConsultaGetDto::new)
                    .collect(Collectors.toList());
    }
	
	
	/**
	 * Lista todas as consultas do usuario logado
	 * Marcadas como Agendada
	 * 
	 */
	public List<ConsultaGetDto> buscarConsultasAgendadasDoUsuario() {
	    UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();
	    PessoaEntity pessoa = usuario.getPessoa();

	    List<ConsultaEntity> consultas = consultaRepository
	        .findByPacienteIdAndStatusConsulta(pessoa.getId(), ConsultaStatus.AGENDADA);

	    return consultas.stream()
	            .map(ConsultaGetDto::new)
	            .collect(Collectors.toList());
	}
	
	/**
	 * Lista todas as consultas do profissional,
	 * marcadas para Hoje
	 * 
	 */
	public List<ConsultaGetDto> buscarConsultasProfissionalHoje() {
		
		UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();
        PessoaEntity pessoa = usuario.getPessoa();

        List<ConsultaEntity> consultas = consultaRepository
    	        .findByProfissionalIdAndDia(pessoa.getProfissionalSaude().getId(), LocalDate.now());
        
            return consultas.stream()
                    .map(ConsultaGetDto::new)
                    .collect(Collectors.toList());
    }



	/**
	 * Profissional atende o Paciente.
	 * Marca a consulta como realizada.
	 * Cria o prontuario.
	 * Cria/Atualiza histórico do Paciente
	 * TODO: Melhorar exception
	 **/
	public void realizarConsulta(Long consultaId, ProntuarioDto dto) {
		// Consulta existe
		ConsultaEntity consulta = existe.consulta(consultaId);

		// Identificação do Profissional
		UsuarioEntity usuarioLogado = usuarioLogadoService.getUsuarioLogado();

		// Profissional é responsável pela consulta
		if (!consulta.getProfissional().getUsuario().getId().equals(usuarioLogado.getId())) {
			throw new AccessDeniedException("Você não tem permissão para alterar esta consulta.");
		}

		// Consulta já foi realizada
		if (consulta.getStatusConsulta() == ConsultaStatus.REALIZADA) {
			throw new IllegalStateException("Esta consulta já foi finalizada.");
		}

		// Consulta foi cancelada ou expirou
		if (!consulta.getStatusConsulta().equals(ConsultaStatus.AGENDADA)) {
			throw new IllegalStateException("Esta consulta não está mais disponivel: " + consulta.getStatusConsulta());
		}

		// Dia da consulta (Desativado)
		//  
		//if (!consulta.getDia().isEqual(LocalDate.now())) {
		//	throw new IllegalStateException("Esta consulta não está agendada para hoje.");
		//}
		
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
	 * 
	 */
	public void cancelarConsultaPaciente(Long consultaId) {
		// Consulta existe
		ConsultaEntity consulta = existe.consulta(consultaId);

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
		
		// Prazo de 24horas para cancelar
		if (!consulta.getDia().isEqual(LocalDate.now())) {
			throw new IllegalStateException("Prazo para cancelamento de consulta acabou.");
		}

		// Marca como cancelada
		consulta.setStatusConsulta(ConsultaStatus.CANCELADA_PACIENTE);
		consultaRepository.save(consulta);

	}
	//TODO: Criar cancelamento pelo profissional e por outro motivo.
}
