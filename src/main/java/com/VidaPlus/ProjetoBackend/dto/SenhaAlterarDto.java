package com.VidaPlus.ProjetoBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SenhaAlterarDto {
	@NotBlank(message = "A senha atual é obrigatória")
	@Size(min = 3, message = "A senha tem que conter no minimo 3 caracteres")
	private String senha;
	
	@NotBlank(message = "A nova senha é obrigatória")
	@Size(min = 3, message = "A senha tem que conter no minimo 3 caracteres")
	private String novaSenha;
	
}
