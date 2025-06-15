package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<ProfissionalSaudeDto> criarProfissional(@RequestBody ProfissionalSaudeDto dto) {
        
    	ProfissionalSaudeDto response = profissionalSaudeService.cadastrarProfissional(dto);
        return ResponseEntity.ok(response);
    }
}
