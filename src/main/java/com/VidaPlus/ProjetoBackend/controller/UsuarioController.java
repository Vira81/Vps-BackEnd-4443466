package com.VidaPlus.ProjetoBackend.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.UsuarioCadastroDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioPerfilDto;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;
import com.VidaPlus.ProjetoBackend.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * Cadastra um novo usuario pelo http://localhost:8080/usuarios/cadastro
	 * 
	 */
	@PostMapping("/cadastro")
	public ResponseEntity<UsuarioEntity> cadastrar(@RequestBody @Valid UsuarioCadastroDto dto) {
		UsuarioEntity novoUsuario = usuarioService.cadastrarNovoUsuario(dto);
		return ResponseEntity.ok(novoUsuario);
	}
	
	

	/**
	 * Cria um novo usuário.
	 */
	@PostMapping
	public ResponseEntity<UsuarioEntity> criarUsuario(@RequestBody UsuarioDto dto) {
		UsuarioEntity salvo = usuarioService.criar(dto);
		return ResponseEntity.ok(salvo);
	}

	/**
	 * Busca um usuário por ID.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioEntity> buscarPorId(@PathVariable Long id) {
		UsuarioEntity usuario = usuarioService.buscarPorId(id);
		return ResponseEntity.ok(usuario);
	}

	/**
	 * Lista todos os usuários.
	 */
	@GetMapping
	public ResponseEntity<List<UsuarioEntity>> listarTodos() {
		List<UsuarioEntity> usuarios = usuarioService.listarTodos();
		return ResponseEntity.ok(usuarios);
	}

	/**
	 * Atualiza os dados de um usuário existente.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioEntity> atualizar(@PathVariable Long id, @RequestBody UsuarioDto dto) {
		UsuarioEntity atualizado = usuarioService.atualizar(id, dto);
		return ResponseEntity.ok(atualizado);
	}

	/**
	 * Remove um usuário pelo ID.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		usuarioService.deletar(id);
		return ResponseEntity.noContent().build(); // HTTP 204
	}

	/**
	 * Somente o ADMIN pode alterar o perfil
	 */
	@PutMapping("/{id}/perfil")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> atualizarPerfil(@PathVariable Long id, @RequestBody UsuarioPerfilDto dto) {
		Optional<UsuarioEntity> optionalUsuario = usuarioRepository.findById(id);

		if (optionalUsuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
		}

		UsuarioEntity usuario = optionalUsuario.get();
		logger.info("Tentando alterar usuário com Id: {}", usuario.getId());
		if (dto.getPerfil() != null) {
			usuario.setPerfil(dto.getPerfil());
		}

		if (dto.getStatus() != null) {
			usuario.setStatus(dto.getStatus());
		}

		usuarioRepository.save(usuario);
		return ResponseEntity.ok("Perfil e/ou status atualizados com sucesso.");
	}
}
