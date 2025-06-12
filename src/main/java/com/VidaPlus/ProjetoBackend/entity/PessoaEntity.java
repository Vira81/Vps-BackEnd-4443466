package com.VidaPlus.ProjetoBackend.entity;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import com.VidaPlus.ProjetoBackend.dto.PessoaDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "vps_pessoa")
@Builder
/**
 * Dados Pessoais do Usuario (Parte '3' da criação do usuario)
 * 
 * Parte 1 - Email / senha : Status: Pendente Parte 2 - Verificação Email :
 * Status: Validado Parte 3 - Dados pessoais : Status: Ativo
 * 
 */

public class PessoaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String nome;

	@Column
	private String cpf;

	@Column
	private LocalDate dataNascimento;

	@Column
	private String telefone;
	
	@OneToOne(mappedBy = "pessoa", fetch = FetchType.LAZY)
	@JsonIgnore //evita loop de JSONs
	private UsuarioEntity usuario;
	
	@OneToOne(mappedBy = "pessoa")
	@JsonIgnore
	private ProfissionalSaudeEntity profissionalSaude;

	// construtor para converter DTO
	public PessoaEntity(PessoaDto usuario) {
		BeanUtils.copyProperties(usuario, this);
	}

}
