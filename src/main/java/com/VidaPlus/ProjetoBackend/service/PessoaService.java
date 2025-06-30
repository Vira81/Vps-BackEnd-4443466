package com.VidaPlus.ProjetoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.PessoaNotNullDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioSaidaDto;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.exception.AlteracaoIndevida;
import com.VidaPlus.ProjetoBackend.repository.PessoaRepository;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Atualiza os dados pessoais do usuario.
     * Por padrão todos os campos são null, no momento que o usuario foi criado.
     * NotNull após essa atualização
     */
    @Autowired
	private UsuarioLogadoService login;
    public PessoaEntity atualizarPessoa(PessoaNotNullDto dto) {
        
    	// Usuario Logado
    	UsuarioEntity usuario = usuarioService.buscarId(login.getUsuarioLogado().getId());
    	PessoaEntity pessoa = usuario.getPessoa();
    	
    	pessoa.setNome(dto.getNome());
    	
    	// Melhorar a validação do CPF
    	if (dto.getCpf() != null && !dto.getCpf().equals(pessoa.getCpf())) {
            if (usuarioRepository.existsByPessoaCpf(dto.getCpf())) {
                throw new AlteracaoIndevida("Este CPF já está em uso.");
            }
            pessoa.setCpf(dto.getCpf());
        }
        pessoa.setTelefone(dto.getTelefone());
        pessoa.setDataNascimento(dto.getDataNascimento());

        return pessoaRepository.save(pessoa);
    }
    
    public UsuarioSaidaDto minhaConta() {
		UsuarioEntity usuario = usuarioRepository.findById(login.getUsuarioLogado().getId())
				.orElseThrow() ;
		return new UsuarioSaidaDto(usuario);
	}
}
