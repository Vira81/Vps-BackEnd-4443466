package com.VidaPlus.ProjetoBackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.AccessDto;
import com.VidaPlus.ProjetoBackend.dto.AuthenticationDto;
import com.VidaPlus.ProjetoBackend.dto.ConsultaDto;
import com.VidaPlus.ProjetoBackend.dto.PessoaNotNullDto;
import com.VidaPlus.ProjetoBackend.dto.ProfissionalSaudeDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioCadastroDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;
import com.VidaPlus.ProjetoBackend.entity.enums.FuncaoSaude;
import com.VidaPlus.ProjetoBackend.repository.ConsultaRepository;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;
import com.VidaPlus.ProjetoBackend.security.JwtUtils;

import jakarta.transaction.Transactional;

@Service
public class TesteAdicionalService {

	@Autowired
	private AuthService authService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private ConsultaService consultaService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private UserDetailServiceImpl userDetailService;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private ProfissionalSaudeService medicoService;

	public UsuarioEntity cadastroCorreto() {
		UsuarioCadastroDto dtoC = new UsuarioCadastroDto();
		String n = UUID.randomUUID().toString().substring(0, 10);
		dtoC.setSenha("123");
		dtoC.setEmail(n + "@vida.com");

		UsuarioEntity novo = usuarioService.cadastrarNovoUsuario(dtoC);
		return novo;
	}

	public UsuarioEntity usuarioAtivado() {
		UsuarioEntity novo = cadastroCorreto();
		usuarioService.ativarConta(novo.getCod());
		return novo;
	}

	public UsuarioEntity usuarioLogado() {

		UsuarioEntity novo = usuarioAtivado();

		// Token
		AuthenticationDto dtoL = new AuthenticationDto();
		dtoL.setUsername(novo.getEmail());
		dtoL.setPassword("123");
		AccessDto token = authService.login(dtoL);

		// "Forçando" a autenticação
		String username = jwtUtils.getUsernameToken(token.getToken());
		UserDetails userDetails = userDetailService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return novo;
	}

	public UsuarioEntity usuarioLogar(Long id) {

		UsuarioEntity uu = usuarioRepository.findById(id).orElseThrow(); 

		// Token
		AuthenticationDto dtoL = new AuthenticationDto();
		dtoL.setUsername(uu.getEmail());
		dtoL.setPassword("123");
		AccessDto token = authService.login(dtoL);

		// "Forçando" a autenticação
		String username = jwtUtils.getUsernameToken(token.getToken());
		UserDetails userDetails = userDetailService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return uu;
	}

	public PessoaEntity pessoaCriada(String nome) {
		usuarioLogado();
		PessoaNotNullDto dto = new PessoaNotNullDto();
		dto.setNome(nome);
		dto.setTelefone("(21)912345678");
		dto.setCpf(UUID.randomUUID().toString().substring(0, 11));
		dto.setDataNascimento(LocalDate.now());
		PessoaEntity pessoaCriada = pessoaService.atualizarPessoa(dto);
		
		return pessoaCriada;
	}

	public ProfissionalSaudeEntity medicoCriado(String nome) {
		PessoaEntity pessoa = pessoaCriada(nome);
		ProfissionalSaudeDto medDto = new ProfissionalSaudeDto();
		medDto.setCpf(pessoa.getCpf());
		medDto.setEspecialidade(EspecialidadeSaude.OUTRA);
		medDto.setCrm("abcdCRM");
		medDto.setFuncao(FuncaoSaude.MEDICA);
		Set<DayOfWeek> dias = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
		medDto.setDiasTrabalho(dias);
		Set<Long> hospitais = Set.of(1L, 2L);
		medDto.setHospitaisIds(hospitais);
		ProfissionalSaudeEntity medico = medicoService.cadastrarProfissional(medDto);
		return medico;
	}
	
	public ConsultaEntity consultaCriada(Long pacienteId, Long medicoId) {

		ConsultaDto dto = new ConsultaDto();
		dto.setPacienteId(pacienteId);
		dto.setProfissionalId(medicoId);
		dto.setHospitalId(1L);
		dto.setDia(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY)));
		dto.setHora(LocalTime.now());

		ConsultaEntity consulta = consultaService.criarConsulta(dto);
		
		return consulta;
	}
	
}
