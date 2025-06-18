package com.VidaPlus.ProjetoBackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAlterarDto {
	@NotBlank(message = "E-mail é obrigatório")
	@Email(message = "E-mail inválido")
	private String email;

}
