package com.VidaPlus.ProjetoBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VidaPlus.ProjetoBackend.dto.BuscaEspecialidadeDto;
import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;
import com.VidaPlus.ProjetoBackend.service.ProfissionalSaudeService;

@RestController
@RequestMapping("/busca")
public class BuscarController {
	@Autowired
	private ProfissionalSaudeService service;

	
	/**
	 * Buscar por especialidade medica , onde {especialidade} é o termo da busca
	 * Ex.: cardiologia, pediatria, etc.
	 * 
	 * GET http://localhost:8080/busca/cardiologia
	 * 
	 */
	@GetMapping("/{especialidade}")
	public ResponseEntity<List<BuscaEspecialidadeDto>> buscarPorEspecialidade(@PathVariable String especialidade) {
	    try {
	        EspecialidadeSaude espEnum = EspecialidadeSaude.valueOf(especialidade.toUpperCase());
	        List<BuscaEspecialidadeDto> lista = service.buscarPorEspecialidade(espEnum);
	        return ResponseEntity.ok(lista);
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().build();
	    }
	}
}
