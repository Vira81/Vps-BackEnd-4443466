package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.ProfissionalSaudeDto;
import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.exception.AlteracaoIndevida;
import com.VidaPlus.ProjetoBackend.repository.HospitalRepository;
import com.VidaPlus.ProjetoBackend.repository.PessoaRepository;
import com.VidaPlus.ProjetoBackend.repository.ProfissionalSaudeRepository;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

@Service
public class ProfissionalSaudeService {

	@Autowired
	private ProfissionalSaudeRepository profissionalSaudeRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	/**
	 * Admin salva os dados do profissional de saude.
	 * 
	 * 
	 */
	public ProfissionalSaudeEntity cadastrarProfissional(ProfissionalSaudeDto dto) {
		// Busca por CPF
		UsuarioEntity usuario = usuarioRepository.findByPessoaCpf(dto.getCpf())
				.orElseThrow(() -> new AlteracaoIndevida("Usuário com esse CPF não foi encontrado"));

		PessoaEntity pessoa = usuario.getPessoa();

		// Ja existe um cadastro
		if (pessoa.getProfissionalSaude() != null) {
			throw new IllegalStateException("Essa pessoa já está cadastrada como profissional de saúde.");
		}

		// Altera o tipo automaticamente se for paciente
		if (usuario.getPerfil() == PerfilUsuario.PACIENTE) {
			usuario.setPerfil(PerfilUsuario.PROFISSIONAL);

		}

		// Salva os dados
		ProfissionalSaudeEntity profissional = new ProfissionalSaudeEntity();
		profissional.setPessoa(pessoa);
		profissional.setUsuario(usuario);
		profissional.setCrm(dto.getCrm());
		profissional.setFuncao(dto.getFuncao());
		profissional.setEspecialidade(dto.getEspecialidade());
		profissional.setDataCriacaoProfissional(LocalDateTime.now());

		// Dias e o hospital aonde o funcionario trabalha
		// Podem ser Nulos (em caso de Telemedicina)
		if (dto.getDiasTrabalho() != null) {
			profissional.setDiasTrabalho(dto.getDiasTrabalho());
		}

		if (dto.getHospitaisIds() != null) {
			Set<Long> ids = dto.getHospitaisIds();

			Set<HospitalEntity> hospitais = new HashSet<>();
			for (Long id : ids) {
				HospitalEntity hospital = hospitalRepository.findById(id)
						.orElseThrow(() -> new AlteracaoIndevida("Hospital id " + id + " não encontrado"));
				hospitais.add(hospital);
			}
			profissional.setHospitais(hospitais);

		}

		return profissionalSaudeRepository.save(profissional);
	}
}
