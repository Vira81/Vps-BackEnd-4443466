package com.VidaPlus.ProjetoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.PessoaDto;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.repository.PessoaRepository;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    /**
     * Atualiza os dados pessoais do usuario.
     * Por padrão todos os campos são null, no momento que o usuario foi criado.
     */
    public PessoaEntity atualizarPessoa(Long id, PessoaDto dto) {
        PessoaEntity pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        if (dto.getNome() != null) pessoa.setNome(dto.getNome());
        if (dto.getCpf() != null) pessoa.setCpf(dto.getCpf());
        if (dto.getTelefone() != null) pessoa.setTelefone(dto.getTelefone());
        if (dto.getDataNascimento() != null) pessoa.setDataNascimento(dto.getDataNascimento());

        return pessoaRepository.save(pessoa);
    }
}
