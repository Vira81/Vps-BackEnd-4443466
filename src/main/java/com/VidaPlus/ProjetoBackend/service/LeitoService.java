package com.VidaPlus.ProjetoBackend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.LeitoGetDto;
import com.VidaPlus.ProjetoBackend.repository.LeitoRepository;

@Service
public class LeitoService {

	@Autowired
	private LeitoRepository leitoRepository;

	/**
	 * Lista todos os leitos
	 * 
	 */
	public List<LeitoGetDto> buscarTodosLeitos() {
		return leitoRepository.findAll().stream().map(LeitoGetDto::new).collect(Collectors.toList());
	}

	public List<LeitoGetDto> buscarLeitosHospital(Long hospitalId) {
		return leitoRepository.findByHospitalId(hospitalId).stream().map(LeitoGetDto::new).collect(Collectors.toList());
	}

}
