package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDate;

import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioSaidaDto {
	private Long id;

	private String email;

	private PerfilUsuario perfil;

	private StatusUsuario status;
	
	private String nome;
	
	private String telefone;
	
	private LocalDate nascimento;
	
	private Long profissional;
	
	private EspecialidadeSaude especialidade;
	
	private String crm;
	
	public UsuarioSaidaDto(UsuarioEntity entity) {
	    this.id = entity.getId();
	    this.email = entity.getEmail();
	    this.perfil = entity.getPerfil();
	    this.status = entity.getStatus();

	    PessoaEntity pessoa = entity.getPessoa();
	    if (pessoa != null) {
	        this.nome = pessoa.getNome();
	        this.telefone = pessoa.getTelefone();
	        this.nascimento = pessoa.getDataNascimento();

	        ProfissionalSaudeEntity prof = pessoa.getProfissionalSaude();
	        if (prof != null) {
	            this.crm = prof.getCrm();
	            this.profissional = prof.getId();
	            this.especialidade = prof.getEspecialidade();
	        }
	    }
	}


}
