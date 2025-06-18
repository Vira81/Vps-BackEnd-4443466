package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaNotNullDto {

	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	
	@NotBlank(message = "CPF é obrigatório")
	@Size(min = 11, message = "O CPF tem que conter no minimo 3 caracteres")
	// TODO: Criar um validador de cpf
	private String cpf;
	
	@NotBlank(message = "Telefone é obrigatório")
	private String telefone;
	
	@NotNull(message = "Nascimento é obrigatório")
	private LocalDate dataNascimento;

}