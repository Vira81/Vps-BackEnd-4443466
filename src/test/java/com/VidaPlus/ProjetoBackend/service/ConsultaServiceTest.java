package com.VidaPlus.ProjetoBackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.VidaPlus.ProjetoBackend.dto.ConsultaDto;
import com.VidaPlus.ProjetoBackend.dto.HistoricoPacienteDto;
import com.VidaPlus.ProjetoBackend.dto.ProfissionalSaudeDto;
import com.VidaPlus.ProjetoBackend.dto.ProntuarioDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioCadastroDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.HistoricoPacienteEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;
import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;
import com.VidaPlus.ProjetoBackend.entity.enums.FuncaoSaude;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.repository.ConsultaRepository;
import com.VidaPlus.ProjetoBackend.repository.HistoricoPacienteRepository;
import com.VidaPlus.ProjetoBackend.repository.PessoaRepository;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@ActiveProfiles("test")
/**
 * Testes de consulta
 */
class ConsultaServiceTest {
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private HistoricoPacienteService historicoService;
	
	@Autowired
	private HistoricoPacienteRepository historicoRepository;
	
	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private ConsultaService consultaService;

	@Autowired
	private ProfissionalSaudeService medicoService;

	@Autowired
	private ValidadorDtoService validar;

	@Autowired
	private TesteAdicionalService add;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private UsuarioLogadoService login;

	@Test
	@DisplayName("Criar profissional saude Basico")
	void medico1() {
		PessoaEntity pessoa = add.pessoaCriada("Medico");
		Long id = pessoa.getId();
		ProfissionalSaudeDto medDto = new ProfissionalSaudeDto();
		medDto.setCpf(pessoa.getCpf());
		medDto.setEspecialidade(EspecialidadeSaude.OUTRA);
		medicoService.cadastrarProfissional(medDto);
		PessoaEntity pessoaAtualizada = pessoaRepository.findById(id).orElseThrow();
		assertEquals(pessoaAtualizada.getUsuario().getPerfil(), PerfilUsuario.PROFISSIONAL);
		assertEquals(pessoaAtualizada.getProfissionalSaude().getEspecialidade(), EspecialidadeSaude.OUTRA);
	}

	@Test
	@DisplayName("Criar profissional invalido")
	void medico2() {
		add.pessoaCriada("Medico");
		ProfissionalSaudeDto medDto = new ProfissionalSaudeDto();
		medDto.setCpf("cpf que nao existe");
		medDto.setEspecialidade(EspecialidadeSaude.OUTRA);
		assertThrows(RuntimeException.class, () -> {
			medicoService.cadastrarProfissional(medDto);
		});

		// tenta criar um profissional ja criado
		medDto.setCpf("22222222222");
		assertThrows(IllegalStateException.class, () -> {
			medicoService.cadastrarProfissional(medDto);
		});

	}

	@Test
	@DisplayName("Criar Consulta com sucesso")
	void consulta1() {
		String nomePaciente = "Aquele que pediu a consulta";
		ProfissionalSaudeEntity medico = add.medicoCriado("Medico da Consulta");
		PessoaEntity paciente = add.pessoaCriada(nomePaciente);

		ConsultaDto dto = new ConsultaDto();
		dto.setPacienteId(paciente.getId());
		dto.setProfissionalId(medico.getId());
		dto.setHospitalId(1L);
		// sempre uma sexta, dia que o medico criado trabalha
		dto.setDia(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY)));
		dto.setHora(LocalTime.now());

		ConsultaEntity consulta = consultaService.criarConsulta(dto);
		assertEquals(consulta.getPaciente().getNome(), nomePaciente);

	}
	
	@Test
	@DisplayName("Realizar Consulta com sucesso")
	@Transactional
	Long realizarConsulta1() {
		String nomePaciente = "Aquele que pediu a consulta";
		PessoaEntity paciente = add.pessoaCriada(nomePaciente);
		ProfissionalSaudeEntity medico = add.medicoCriado("Medico da Consulta");
		ConsultaEntity consulta = add.consultaCriada(paciente.getId(),medico.getId());
		ProntuarioDto dto = new ProntuarioDto();
		dto.setDiagnostico("abc");
		dto.setObservacao("def");
		// o medico certo está logado
		consultaService.realizarConsulta(consulta.getId(), dto);
		
		ConsultaEntity consultaAtualizada = consultaRepository.findById(consulta.getId()).orElseThrow();
		assertEquals(consultaAtualizada.getStatusConsulta(),ConsultaStatus.REALIZADA);
		assertNotNull(consultaAtualizada.getProntuario());
		System.out.println(consultaAtualizada.getProntuario().getDiagnostico());
	return paciente.getId();
	}
	
	//@Test
	@Transactional
	@DisplayName("Checar o Historico")
	void historico() {
		Long pacienteId = realizarConsulta1();
		
		add.usuarioLogar(pacienteId);
		HistoricoPacienteDto historico = historicoService.buscarHistoricoPaciente();
		
		System.out.println(historico);
		assertNotNull(historico.getEntradas());

	}
}