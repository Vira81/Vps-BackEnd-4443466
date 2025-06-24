package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.ProntuarioDto;
import com.VidaPlus.ProjetoBackend.dto.TeleconsultaDto;
import com.VidaPlus.ProjetoBackend.dto.TeleconsultaSaidaDto;
import com.VidaPlus.ProjetoBackend.service.TeleconsultaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/teleconsulta")
public class TeleconsultaController {

    @Autowired
    private TeleconsultaService teleconsultaService;

    /**
     * Agendamento da teleconsulta
     * 
     * POST http://localhost:8080/teleconsulta/criar
     * 
     * {    "profissionalId":1,    "dia":"2025-06-24"}
     */
    @PostMapping("/criar")
    public ResponseEntity<TeleconsultaSaidaDto> agendar(@RequestBody @Valid TeleconsultaDto dto) {
        TeleconsultaSaidaDto nova = teleconsultaService.agendarTeleconsulta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nova);
    }
    
    /**
     * O paciente inicia a teleconsulta
     * 
     * GET no link gerado na criação
     *
     */
    @GetMapping("/sala/{roomId}")
    public ResponseEntity<String> entrar(@PathVariable String roomId) {
        teleconsultaService.realizarTeleconsulta(roomId);
        return ResponseEntity.ok("Teleconsulta em andamento.");
    }
    
    /**
     * O profissional entra na teleconsulta e a finaliza mandando o prontuario
     * A prescrição fica igual a consulta normal.
     * 
     * POST no link gerado na criação
     * { "diagnostico": "Dolor",
	 * "observacao": "sit amet"}
     *
     */
    @PostMapping("/sala/{roomId}")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ResponseEntity<String> entrarProf(@PathVariable String roomId, @RequestBody ProntuarioDto dto) {
        teleconsultaService.realizarTeleconsultaProf(roomId, dto);
        return ResponseEntity.ok("Teleconsulta finalizada.");
    }
    
}
