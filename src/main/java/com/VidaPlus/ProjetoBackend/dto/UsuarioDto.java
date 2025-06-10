package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;

import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
/**
 * TODO: Alterar o dto, nao passar senhas
 */

public class UsuarioDto {

	private Long id;

	private String email;

	private String senhaHash;

	private PerfilUsuario perfil;

	private LocalDateTime ultimoAcesso;

	private LocalDateTime dataCriacao;

	public UsuarioDto(UsuarioEntity usuario) {
		BeanUtils.copyProperties(usuario, this);
	}

}
