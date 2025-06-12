package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.PessoaDto;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
@CrossOrigin
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    /**
     * O usuario somente pode alterar os proprios dados
     * TODO: Fazer isso tambem para Usuario (login)
     */
    @PutMapping("/{id}")
    @PreAuthorize("@usuarioService.usuarioPodeAlterar(#id)")
    public ResponseEntity<PessoaEntity> atualizarPessoa(
            @PathVariable Long id,
            @RequestBody PessoaDto dto) {

        PessoaEntity pessoaAtualizada = pessoaService.atualizarPessoa(id, dto);
        return ResponseEntity.ok(pessoaAtualizada);
    }
}
