package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.BuscaEspecialidadeDto;
import com.VidaPlus.ProjetoBackend.dto.ProfissionalSaudeDto;
import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.exception.AlteracaoIndevida;
import com.VidaPlus.ProjetoBackend.repository.HospitalRepository;
import com.VidaPlus.ProjetoBackend.repository.ProfissionalSaudeRepository;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

@Service
public class ProfissionalSaudeService {

	@Autowired
	private ProfissionalSaudeRepository profissionalSaudeRepository;

	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ExisteService existe;
	/**
	 * Admin salva os dados do profissional de saude.
	 * 
	 * Comportamento: Um admin pode virar profissional de saude e se manter
	 * como admin. TODO: Verificar se o Admin-Profissional tem as permissões
	 * para as duas funções.
	 */
	public ProfissionalSaudeEntity cadastrarProfissional(ProfissionalSaudeDto dto) {
		// Busca por CPF
		UsuarioEntity usuario = existe.usuarioCpf(dto.getCpf());

		PessoaEntity pessoa = usuario.getPessoa();

		// Ja existe um cadastro
		if (pessoa.getProfissionalSaude() != null) {
			throw new IllegalStateException("Essa pessoa já está cadastrada como profissional de saúde.");
		}

		// Altera o tipo automaticamente se for paciente
		// Não muda se for Admin
		if (usuario.getPerfil() == PerfilUsuario.PACIENTE) {
			usuario.setPerfil(PerfilUsuario.PROFISSIONAL);
			usuarioRepository.save(usuario);

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
	
	public ProfissionalSaudeEntity atualizarProfissional(ProfissionalSaudeDto dto) {
		// Busca por CPF
		UsuarioEntity usuario = existe.usuarioCpf(dto.getCpf());

		ProfissionalSaudeEntity profissional = usuario.getPessoa().getProfissionalSaude();

		// Atualiza os dados
		if (dto.getCrm() != null) {profissional.setCrm(dto.getCrm());}
		if (dto.getFuncao() != null) {profissional.setFuncao(dto.getFuncao());}
		if (dto.getEspecialidade() != null) {profissional.setEspecialidade(dto.getEspecialidade());}

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

	// Busca informações do medico por Especialidade
	public List<BuscaEspecialidadeDto> buscarPorEspecialidade(EspecialidadeSaude especialidade) {
        List<ProfissionalSaudeEntity> lista = profissionalSaudeRepository.findByEspecialidade(especialidade);

        return lista.stream()
                    .map(this::buscaEspecialidadeSaida)
                    .collect(Collectors.toList());
    }
    
    // Saida da busca por especialidade
	public BuscaEspecialidadeDto buscaEspecialidadeSaida(ProfissionalSaudeEntity entity) {
    	BuscaEspecialidadeDto dto = new BuscaEspecialidadeDto();
      
        dto.setEspecialidade(entity.getEspecialidade());
        dto.setCrm(entity.getCrm());
        

        if (entity.getPessoa() != null) {
            dto.setMedico(entity.getPessoa().getNome());
        }
        
        if (entity.getHospitais() != null) {
            List<String> nomesHospitais = entity.getHospitais()
                                                .stream()
                                                .map(HospitalEntity::getNome)
                                                .collect(Collectors.toList());
            dto.setHospitais(nomesHospitais);
        }

        if (entity.getDiasTrabalho() != null) {
            dto.setDiasAtendimento(new ArrayList<>(entity.getDiasTrabalho()));
        }


        return dto;
    }
}
