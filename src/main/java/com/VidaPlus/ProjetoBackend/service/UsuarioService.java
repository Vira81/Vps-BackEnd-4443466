package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.UsuarioDto;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UsuarioService {
	private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	private final UsuarioRepository usuarioRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	/**
	 * Criação do usuario
	 * Um usuario comum só pode criar logins do tipo PACIENTE 
	 * @param dto
	 * @return Usuario Salvo
	 */
	public UsuarioEntity criar(UsuarioDto dto) {
		logger.info("Tentando criar usuário com e-mail: {}", dto.getEmail());

		if (usuarioRepository.existsByEmail(dto.getEmail())) {
			logger.warn("Usuário com e-mail {} já está cadastrado", dto.getEmail());
			throw new RuntimeException("E-mail já cadastrado");
		}

		if (dto.getSenhaHash() == null || dto.getSenhaHash().isBlank()) {
			logger.error("Senha nula ou vazia para o e-mail: {}", dto.getEmail());
			throw new IllegalArgumentException("A senha não pode ser nula ou vazia");
		}

		UsuarioEntity usuario = UsuarioEntity.builder().email(dto.getEmail())
				.senhaHash(passwordEncoder.encode(dto.getSenhaHash()))
				.perfil(dto.getPerfil() != null ? dto.getPerfil() : PerfilUsuario.PACIENTE)
				.dataCriacao(LocalDateTime.now()).build();

		UsuarioEntity salvo = usuarioRepository.save(usuario);
		logger.info("Usuário criado com ID: {}", salvo.getId());

		return salvo;
	}

	// Buscar por ID
	public UsuarioEntity buscarPorId(Long id) {
		return usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
	}

	// Listar todos usuarios
	public List<UsuarioEntity> listarTodos() {
		return usuarioRepository.findAll();
	}

	// Atualizar usuario
	public UsuarioEntity atualizar(Long id, UsuarioDto dto) {
		UsuarioEntity usuario = buscarPorId(id);

		if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
			if (usuarioRepository.existsByEmail(dto.getEmail())) {
				throw new RuntimeException("E-mail já está em uso");
			}
			usuario.setEmail(dto.getEmail());
		}

		if (dto.getPerfil() != null) {
			usuario.setPerfil(dto.getPerfil());
		}

		return usuarioRepository.save(usuario);
	}

	// Atualizar senha
	public void alterarSenha(Long id, String novaSenha) {
		UsuarioEntity usuario = buscarPorId(id);
		usuario.setSenhaHash(passwordEncoder.encode(novaSenha));
		usuarioRepository.save(usuario);
	}

	/**
	 * Deletar usuario por Id
	 * TODO: Realizar mais testes 
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
}
