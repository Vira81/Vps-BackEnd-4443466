package com.VidaPlus.ProjetoBackend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.TeleconsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.repository.ConsultaRepository;
import com.VidaPlus.ProjetoBackend.repository.HospitalRepository;
import com.VidaPlus.ProjetoBackend.repository.PessoaRepository;
import com.VidaPlus.ProjetoBackend.repository.ProfissionalSaudeRepository;
import com.VidaPlus.ProjetoBackend.repository.TeleconsultaRepository;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

@Service
public class ExisteService {

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private ProfissionalSaudeRepository profissionalRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private TeleconsultaRepository teleconsultaRepository;
	

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
    
    public HospitalEntity hospitalVirtual(Long hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital Virtual não encontrado. Verifique as configurações."));
    }
    
    public TeleconsultaEntity teleconsulta(String salaId) {
        return teleconsultaRepository.findBySalaId(salaId)
                .orElseThrow(() -> new RuntimeException("Teleconsulta não encontrada."));
    }
    
    public ConsultaEntity consulta(Long consultaId) {
        return consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }
    
    public UsuarioEntity usuarioCpf(String cpf) {
        return usuarioRepository.findByPessoaCpf(cpf)
                .orElseThrow(() -> new RuntimeException("CPF não encontrado"));
    }
}
