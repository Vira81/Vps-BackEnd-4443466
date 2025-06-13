package com.VidaPlus.ProjetoBackend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.ConsultaDto;
import com.VidaPlus.ProjetoBackend.entity.*;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.repository.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ConsultaService {

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private ProfissionalSaudeRepository profissionalRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private HospitalRepository hospitalRepository;

	@Transactional
	public ConsultaEntity criarConsulta(ConsultaDto dto) {

		ProfissionalSaudeEntity profissional = profissionalRepository.findById(dto.getProfissionalId())
				.orElseThrow(() -> new RuntimeException("Profissional não encontrado."));

		// verifica perfil de profissional
		if (profissional.getUsuario().getPerfil() != PerfilUsuario.PROFISSIONAL) {
			throw new RuntimeException("Usuário não é profissional de saude.");
		}

		PessoaEntity paciente = pessoaRepository.findById(dto.getPacienteId())
				.orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

		HospitalEntity hospital = hospitalRepository.findById(dto.getHospitalId())
				.orElseThrow(() -> new RuntimeException("Hospital não existe."));

		// Cria a consulta
		ConsultaEntity novaConsulta = ConsultaEntity.builder().profissional(profissional).paciente(paciente)
				.hospital(hospital).dia(dto.getDia()).hora(dto.getHora()).valor(dto.getValor()).build();

		return consultaRepository.save(novaConsulta);
	}

	public ConsultaEntity buscarPorId(Long id) {
		return consultaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada"));
	}
}
