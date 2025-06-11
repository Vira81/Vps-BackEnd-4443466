package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protegido")
public class ProtegidoController {

	/**
     * Teste de login e token (Corrigido)
     * Token gerado e validado com sucesso
     * http://localhost:8080/protegido/token
     */
    @GetMapping("/token")
    public ResponseEntity<String> testeProtegido() {
        return ResponseEntity.ok("Acesso autorizado! Token Validado!");
    }
}
