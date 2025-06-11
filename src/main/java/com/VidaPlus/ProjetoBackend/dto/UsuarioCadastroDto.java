package com.VidaPlus.ProjetoBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Cadastro de usuario Use email, senha, perfil, status no JSON
 */
public class UsuarioCadastroDto {
	private String email;
	private String senha;

}
