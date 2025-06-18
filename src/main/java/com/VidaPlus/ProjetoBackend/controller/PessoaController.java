package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.PessoaNotNullDto;
import com.VidaPlus.ProjetoBackend.service.PessoaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/dados_pessoais")
@CrossOrigin
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    /**
     * O usuario somente pode alterar os proprios dados
     * TODO: Fazer isso tambem para Usuario (login)
     */
    @PutMapping
    public ResponseEntity<String> atualizarPessoa(@Valid @RequestBody PessoaNotNullDto dto) {

        pessoaService.atualizarPessoa(dto);
        return ResponseEntity.ok("Dados Atualizados com sucesso!");
    }
}
