package com.VidaPlus.ProjetoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.TeleconsultaDto;
import com.VidaPlus.ProjetoBackend.dto.TeleconsultaSaidaDto;
import com.VidaPlus.ProjetoBackend.service.TeleconsultaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/teleconsulta")
public class TeleconsultaController {

    @Autowired
    private TeleconsultaService teleconsultaService;

    @PostMapping("/criar")
    public ResponseEntity<TeleconsultaSaidaDto> agendar(@RequestBody @Valid TeleconsultaDto dto) {
        TeleconsultaSaidaDto nova = teleconsultaService.agendarTeleconsulta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nova);
    }
    
    @PostMapping("/sala/{roomId}")
    public ResponseEntity<TeleconsultaSaidaDto> realizar(@PathVariable String roomId) {
        TeleconsultaSaidaDto dto = teleconsultaService.realizarTeleconsulta(roomId);
        return ResponseEntity.ok(dto);
    }
}
