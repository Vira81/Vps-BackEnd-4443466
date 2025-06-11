package com.VidaPlus.ProjetoBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.service.UsuarioService;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Cadastra um novo usuario pelo
     * http://localhost:8080/usuarios/cadastro
     * 
     */
    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioEntity> cadastrar(@RequestBody UsuarioCadastroDto dto) {
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
}
