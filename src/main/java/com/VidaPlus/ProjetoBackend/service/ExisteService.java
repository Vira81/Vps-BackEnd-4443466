package com.VidaPlus.ProjetoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.repository.ConsultaRepository;
import com.VidaPlus.ProjetoBackend.repository.HospitalRepository;
import com.VidaPlus.ProjetoBackend.repository.PessoaRepository;
import com.VidaPlus.ProjetoBackend.repository.ProfissionalSaudeRepository;

@Service
public class ExisteService {

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private ProfissionalSaudeRepository profissionalRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private ProntuarioService prontuarioService;

	@Autowired
	private HistoricoPacienteService historicoPacienteService;

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;
	

    public ProfissionalSaudeEntity profissionalSaude(Long profissionalId) {
        return profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
    }
    
    public PessoaEntity paciente(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    public HospitalEntity hospital(Long hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital não encontrado"));
    }
    
    public ConsultaEntity consulta(Long consultaId) {
        return consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }
}
