package com.VidaPlus.ProjetoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.ProfissionalSaudeDto;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
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
    private PessoaRepository pessoaRepository;

    public ProfissionalSaudeDto cadastrar(ProfissionalSaudeDto dto) {
        UsuarioEntity usuario = usuarioRepository.findById(dto.getUsuarioId())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        PessoaEntity pessoa = pessoaRepository.findById(dto.getPessoaId())
            .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        if (!"PROFISSIONAL".equalsIgnoreCase(usuario.getPerfil().name())) {
            throw new RuntimeException("O usuário não é Profissional de Saude.");
        }
        ProfissionalSaudeEntity profissional = new ProfissionalSaudeEntity();
        profissional.setUsuario(usuario);
        profissional.setPessoa(pessoa);
        profissional.setEspecialidade(dto.getEspecialidade());
        profissional.setCrm(dto.getCrm());

        ProfissionalSaudeEntity salvo = profissionalSaudeRepository.save(profissional);

        ProfissionalSaudeDto response = new ProfissionalSaudeDto();
        response.setId(salvo.getId());
        response.setEspecialidade(salvo.getEspecialidade());
        response.setCrm(salvo.getCrm());
        response.setDataCriacaoProfissional(salvo.getDataCriacaoProfissional());

        return response;
    }
}
