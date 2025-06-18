package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.ProfissionalSaudeDto;
import com.VidaPlus.ProjetoBackend.service.ProfissionalSaudeService;

@RestController
@RequestMapping("/profissional")
public class ProfissionalSaudeController {

    @Autowired
    private ProfissionalSaudeService profissionalSaudeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> criarProfissional(@RequestBody ProfissionalSaudeDto dto) {
        
    	profissionalSaudeService.cadastrarProfissional(dto);
        return ResponseEntity.ok("Funcionario cadastrado com sucesso.");
    }
    
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> alterarProfissional(@RequestBody ProfissionalSaudeDto dto) {
        
    	profissionalSaudeService.cadastrarProfissional(dto);
        return ResponseEntity.ok("Funcionario atualizado com sucesso.");
    }
    
    //TODO: Criar PUT
}
