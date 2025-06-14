package com.VidaPlus.ProjetoBackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Cadastro de usuario Use email, senha, perfil, status no JSON 
 */
public class UsuarioCadastroDto {
	@NotBlank(message = "E-mail é obrigatório")
	@Email(message = "E-mail inválido")
	private String email;
	
	@NotBlank(message = "Senha é obrigatória")
	@Size(min = 3, message = "A senha tem que conter no minimo 3 caracteres")
	private String senha;
	
}
