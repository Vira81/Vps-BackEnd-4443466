package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.VidaPlus.ProjetoBackend.dto.EmailAlterarDto;
import com.VidaPlus.ProjetoBackend.dto.SenhaAlterarDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioCadastroDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioPerfilDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioSaidaDto;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;
import com.VidaPlus.ProjetoBackend.exception.AlteracaoIndevida;
import com.VidaPlus.ProjetoBackend.exception.EmailJaCadastradoException;
import com.VidaPlus.ProjetoBackend.repository.PessoaRepository;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UsuarioService {
	private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private UsuarioLogadoService login;

	/**
	 * O usuario altera o email usado para logar
	 * O token antigo será invalido, logar usando o email novo.
	 */
	public ResponseEntity<?> atualizarEmail(EmailAlterarDto dto) {
		UsuarioEntity usuario = buscarId(login.getUsuarioLogado().getId());

		if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
			if (usuarioRepository.existsByEmail(dto.getEmail())) {
				throw new EmailJaCadastradoException("E-mail já está em uso");
			}
			usuario.setEmail(dto.getEmail());
		}

		usuarioRepository.save(usuario);
		return ResponseEntity.ok("Email atualizado com sucesso");
	}

	// Buscar Usuario por Id
	public UsuarioEntity buscarId(Long id) {
		return usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
	}
	
	/**
	 * Buscar Usuario por Id
	 * Usado para controlar a saida de dados sensiveis
	 */
	public UsuarioSaidaDto buscarPorId(Long id) {
		UsuarioEntity usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
		return new UsuarioSaidaDto(usuario);
	}

	/**
	 * Lista todos os usuarios
	 * usando o Dto de saida
	 */
	@Transactional
	public List<UsuarioSaidaDto> listarTodosUsuarios() {
	    List<UsuarioEntity> usuarios = usuarioRepository.findAllSaida(); 
	    return usuarios.stream()
	                   .map(UsuarioSaidaDto::new)
	                   .toList();
	}


	public ResponseEntity<?> atualizarSenha(SenhaAlterarDto dto) {
		UsuarioEntity usuario = buscarId(login.getUsuarioLogado().getId());
		
		//usuario.setSenhaHash(passwordEncoder.encode(novaSenha));

		usuarioRepository.save(usuario);
		return ResponseEntity.ok("Senha atualizada com sucesso");
	}
	
	// Atualizar senha
	public void alterarSenha(Long id, String novaSenha) {
	//	UsuarioEntity usuario = buscarId(id);
	//	usuario.setSenhaHash(passwordEncoder.encode(novaSenha));
	//	usuarioRepository.save(usuario);
	}

	/**
	 * Deletar usuario por Id TODO: Realizar mais testes
	 * 
	 * @param id
	 */
	public void deletar(Long id) {
		if (!usuarioRepository.existsById(id)) {
			throw new RuntimeException("Usuário com ID " + id + " não encontrado");
		}

		usuarioRepository.deleteById(id);
		logger.info("Usuário com ID {} foi deletado", id);
	}

	// Buscar por email
	public Optional<UsuarioEntity> buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	// Atualizar último acesso
	public void atualizarUltimoAcesso(UsuarioEntity usuario) {
		usuario.setUltimoAcesso(LocalDateTime.now());
		usuarioRepository.save(usuario);
	}

	public UsuarioEntity cadastrarNovoUsuario(UsuarioCadastroDto dto) {
		if (usuarioRepository.existsByEmail(dto.getEmail())) {
			throw new EmailJaCadastradoException("O e-mail informado já está cadastrado.");
		}

		UsuarioEntity novoUsuario = UsuarioEntity.builder().email(dto.getEmail())
				.senhaHash(passwordEncoder.encode(dto.getSenha())).perfil(PerfilUsuario.PACIENTE)
				.status(StatusUsuario.PENDENTE).pessoa(new PessoaEntity()).build();

		return usuarioRepository.save(novoUsuario);
	}

	public boolean usuarioPodeAlterar(Long pessoaId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String emailLogado = auth.getName();

		return pessoaRepository.findById(pessoaId).map(pessoa -> pessoa.getUsuario().getEmail().equals(emailLogado))
				.orElseThrow(() -> new AlteracaoIndevida("Somente é possivel alterar os seus dados"));
	}

	public ResponseEntity<?> atualizarPerfil(Long id, UsuarioPerfilDto dto) {
		UsuarioEntity usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		usuario.setPerfil(dto.getPerfil());
		usuarioRepository.save(usuario);

		return ResponseEntity.ok("Perfil atualizado com sucesso");
	}

}
