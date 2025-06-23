package com.VidaPlus.ProjetoBackend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.EmailDto;
import com.VidaPlus.ProjetoBackend.dto.SenhaAlterarDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioCadastroDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioPerfilDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioSaidaDto;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;
import com.VidaPlus.ProjetoBackend.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")

// TODO: colocar autorizações, melhorar metodos genericos 
public class UsuarioController {
	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private UsuarioService usuarioService;

	/**
	 * Cadastra um novo usuario 
	 * 
	 * POST http://localhost:8080/usuarios/cadastro
	 * {  "email": "usuario@example.com",  "senha": "senha123"}
	 */
	@PostMapping("/cadastro")
	public ResponseEntity<String> cadastrar(@RequestBody @Valid UsuarioCadastroDto dto) {
		usuarioService.cadastrarNovoUsuario(dto);
		return ResponseEntity.ok("Usuario cadastrado com sucesso!");
	}

	/**
	 * Busca um usuário por ID.
	 * 
	 * GET http://localhost:8080/usuarios/1
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UsuarioSaidaDto> buscarPorId(@PathVariable Long id) {
		UsuarioSaidaDto usuario = usuarioService.buscarPorId(id);
		return ResponseEntity.ok(usuario);
	}

	/**
	 * Lista todos os usuários.
	 * 
	 * GET http://localhost:8080/usuarios/
	 */
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UsuarioSaidaDto>> listarTodos() {
		return ResponseEntity.ok(usuarioService.listarTodosUsuarios());
	}

	/**
	 * Atualiza o e-mail do usuario logado
	 * 
	 * PUT http://localhost:8080/usuarios/atualizar_email
	 * 
	 * {"email":"novo@vida.com"}
	 */
	@PutMapping("/atualizar_email")
	public ResponseEntity<String> atualizarEmail(@Valid @RequestBody EmailDto dto) {
		usuarioService.atualizarEmail(dto);
		return ResponseEntity.ok("Email atualizado com sucesso");
	}

	/**
	 * Marca a conta como Inativa
	 * 
	 * O sistema não apresentara comandos Delete,
	 * contas, consultas, etc serão marcadas como cancelada, inativa...
	 * 
	 * PUT http://localhost:8080/usuarios/desativar-conta
	 * 
	 * TODO: Implementar a logica do login e logout
	 */
	@PutMapping("/desativar-conta")
	public ResponseEntity<String> desativar() {
		usuarioService.desativarUsuario();
		return ResponseEntity.ok("Conta desativada!");
	}

	/**
	 * Altera a senha do usuario logado
	 * 
	 * PUT http://localhost:8080/usuarios/atualizar_senha
	 * 
	 * {"senha":"123", "novaSenha":"1234"}
	 */
	@PutMapping("/atualizar_senha")
	public ResponseEntity<String> atualizarSenha(@Valid @RequestBody SenhaAlterarDto dto) {
		usuarioService.atualizarSenha(dto);
		return ResponseEntity.ok("Senha atualizada com sucesso");
	}

	/**
	 * Busca usuario pelo Email exato
	 * 
	 * GET http://localhost:8080/usuarios/buscar/email
	 * 
	 * Params
	 * Key: email value: admin@vida.com
	 * http://localhost:8080/usuarios/buscar/email?email=admin@vida.com
	 */
	@GetMapping("/buscar/email")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UsuarioSaidaDto> buscarPorEmail(@RequestParam String email) {
		return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
	}

	/**
	 * Busca usuario por uma parte do nome
	 * 
	 * GET http://localhost:8080/usuarios/buscar/nome
	 * 
	 * Params
	 * Key: nome value: med
	 * http://localhost:8080/usuarios/buscar/nome?nome=med
	 */
	@GetMapping("/buscar/nome")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UsuarioSaidaDto>> buscarPorNome(@RequestParam String nome) {
		return ResponseEntity.ok(usuarioService.buscarPorNome(nome));
	}

	/**
	 * Altera o perfil de um usuario
	 * 
	 * GET http://localhost:8080/usuarios/perfil/alterar
	 * {"cpf":"00000000000","perfil": "ADMIN", "status":"INATIVO"}
	 */
	@PutMapping("/perfil/alterar")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UsuarioSaidaDto> atualizarPerfilPorCpf(@RequestBody @Valid UsuarioPerfilDto dto) {
		UsuarioSaidaDto atualizado = usuarioService.atualizarPerfilPorCpf(dto);
		return ResponseEntity.ok(atualizado);
	}

}
