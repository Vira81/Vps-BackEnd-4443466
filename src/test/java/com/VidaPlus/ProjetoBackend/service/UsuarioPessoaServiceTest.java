package com.VidaPlus.ProjetoBackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.VidaPlus.ProjetoBackend.dto.AccessDto;
import com.VidaPlus.ProjetoBackend.dto.AuthenticationDto;
import com.VidaPlus.ProjetoBackend.dto.EmailDto;
import com.VidaPlus.ProjetoBackend.dto.PessoaNotNullDto;
import com.VidaPlus.ProjetoBackend.dto.SenhaAlterarDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioCadastroDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioSaidaDto;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;
import com.VidaPlus.ProjetoBackend.exception.AlteracaoIndevida;
import com.VidaPlus.ProjetoBackend.exception.EmailJaCadastradoException;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@ActiveProfiles("test")
/**
 * Testes de cadastro, login, dados pessoais
 */
class UsuarioPessoaServiceTest {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private AuthService authService;

	@Autowired
	private ValidadorDtoService validar;

	@Autowired
	private TesteAdicionalService add;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioLogadoService login;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("Cadastrar Usuario: Dados Validos")
	void cadastrar1() {
		UsuarioCadastroDto teste = new UsuarioCadastroDto();
		teste.setEmail("teste@teste.com");
		teste.setSenha("123");
		validar.dto(teste);
		UsuarioEntity cadastro = usuarioService.cadastrarNovoUsuario(teste);

		assertNotNull(cadastro.getId());
		assertEquals(teste.getEmail(), cadastro.getEmail());
		assertNotNull(cadastro.getCod());
	}

	@Test
	@DisplayName("Cadastrar Usuario: Dados invalidos")
	void cadastrar2() {
		UsuarioCadastroDto teste = new UsuarioCadastroDto();
		// Vazio
		teste.setEmail("");
		teste.setSenha("123");
		assertThrows(ConstraintViolationException.class, () -> {
			validar.dto(teste);
		});

		// Nulo
		teste.setEmail(null);
		assertThrows(ConstraintViolationException.class, () -> {
			validar.dto(teste);
		});
	}

	@Test
	@DisplayName("Cadastrar Usuario: Tentar mudar parametro")
	void cadastrar3() {
		UsuarioCadastroDto teste = new UsuarioCadastroDto();
		teste.setEmail("teste3@teste.com");
		teste.setSenha("123");
		// tenta enviar um parametro
		teste.setCod("aaaa");

		validar.dto(teste);
		UsuarioEntity cadastro = usuarioService.cadastrarNovoUsuario(teste);
		assertNotEquals(teste.getCod(), cadastro.getCod());
	}

	@Test
	@DisplayName("Ativar Usuario com sucesso")
	void ativar1() {
		UsuarioEntity usuario = add.cadastroCorreto();
		usuarioService.ativarConta(usuario.getCod());

		UsuarioEntity atualizado = usuarioRepository.findById(usuario.getId()).orElseThrow();
		assertEquals(atualizado.getStatus(), StatusUsuario.ATIVO);
		assertNull(atualizado.getCod());
	}

	@Test
	@DisplayName("Ativar Usuario com codigo invalido")
	void ativar2() {
		UsuarioEntity usuario = add.cadastroCorreto();
		String codInvalido = usuario.getCod() + "a";
		assertThrows(AlteracaoIndevida.class, () -> {
			usuarioService.ativarConta(codInvalido);
		});

	}

	@Test
	@DisplayName("Fazer login com sucesso")
	void Login1() {
		UsuarioEntity novo = add.usuarioAtivado();
		AuthenticationDto dtoL = new AuthenticationDto();
		dtoL.setUsername(novo.getEmail());
		dtoL.setPassword("123");
		AccessDto token = authService.login(dtoL);

		assertNotNull(token.getToken());
	}

	@Test
	@DisplayName("Fazer login com dados errados")
	void Login2() {
		UsuarioEntity novo = add.usuarioAtivado();
		AuthenticationDto dtoL = new AuthenticationDto();
		dtoL.setUsername(novo.getEmail());
		dtoL.setPassword("senha errada");
		assertThrows(AccessDeniedException.class, () -> {
			authService.login(dtoL);
		});

	}

	@Test
	@DisplayName("Usuario está logado?")
	// Esse usuarioLogado força usuario para os testes.
	void usuarioEstaLogado() {
		UsuarioEntity usuario = add.usuarioLogado();
		assertEquals(login.getUsuarioLogado().getEmail(), usuario.getEmail());

	}

	@Test
	@DisplayName("Alterar Dados e Minha Conta")
	void alterarDados() {
		PessoaNotNullDto dto = new PessoaNotNullDto();
		dto.setNome("Testando da Silva");
		dto.setTelefone("(21)912345678");
		// TODO: fazer a validação de cpf
		dto.setCpf("qawsedrftgy");
		dto.setDataNascimento(LocalDate.now());
		validar.dto(dto);
		pessoaService.atualizarPessoa(dto);
		assertNotNull(dto.getNome());

		UsuarioSaidaDto conta = pessoaService.minhaConta();
		assertEquals(dto.getNome(), conta.getNome());

	}

	@Test
	@DisplayName("Alterar Dados Incorretamente (Faltando nome - cpf ja em uso)")
	void alterarDados2() {
		add.usuarioLogado();
		PessoaNotNullDto dto = new PessoaNotNullDto();
		// falta o nome
		dto.setTelefone("(21)912345678");
		// cpf ja presente no banco
		dto.setCpf("00000000000");
		dto.setDataNascimento(LocalDate.now());
		assertThrows(ConstraintViolationException.class, () -> {
			validar.dto(dto);
		});

		dto.setNome("Nome agora");
		validar.dto(dto);
		assertThrows(AlteracaoIndevida.class, () -> {
			pessoaService.atualizarPessoa(dto);
		});
	}

	@Test
	@DisplayName("Alterar Email e Senha")
	void alterarEmailSenha() {
		UsuarioEntity usuario = add.usuarioLogado();
		String email = usuario.getEmail();
		SenhaAlterarDto dtoS = new SenhaAlterarDto();
		dtoS.setSenha("123");
		dtoS.setNovaSenha("1234");

		usuarioService.atualizarSenha(dtoS);
		UsuarioEntity senhaNova = usuarioRepository.findByEmail(email).orElseThrow();
		assertTrue(passwordEncoder.matches("1234", senhaNova.getSenhaHash()));

		EmailDto dtoE = new EmailDto();
		String emailAlterado = "Email@Alterado.com";
		dtoE.setEmail(emailAlterado);
		usuarioService.atualizarEmail(dtoE);
		// Email antigo
		assertThrows(NoSuchElementException.class, () -> {
			usuarioRepository.findByEmail(email).orElseThrow();

		});
		UsuarioEntity emailA = usuarioRepository.findByEmail(emailAlterado).orElseThrow();
		assertNotNull(emailA.getId());
	}

	@Test
	@DisplayName("Alterar Email e Senha incorretamente")
	void alterarEmailSenha2() {
		add.usuarioLogado();
		SenhaAlterarDto dtoS = new SenhaAlterarDto();
		dtoS.setSenha("senha antiga errada");
		dtoS.setNovaSenha("1234");
		assertThrows(AlteracaoIndevida.class, () -> {
			usuarioService.atualizarSenha(dtoS);
		});
		EmailDto dtoE = new EmailDto();
		String emailAlterado = "EmailAlteradoIncorretamente";
		dtoE.setEmail(emailAlterado);
		assertThrows(ConstraintViolationException.class, () -> {
			validar.dto(dtoE);
		});

		String emailExistente = "admin@vida.com";
		dtoE.setEmail(emailExistente);
		assertThrows(EmailJaCadastradoException.class, () -> {
			usuarioService.atualizarEmail(dtoE);
		});
	}
}