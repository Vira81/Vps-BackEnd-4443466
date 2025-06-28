package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;

import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class UsuarioDto {

	private Long id;

	@Email
	private String email;

	@NotNull
	private String senhaHash;

	private PerfilUsuario perfil;

	private StatusUsuario status;

	private LocalDateTime ultimoAcesso;

	private LocalDateTime dataCriacao;

	public UsuarioDto(UsuarioEntity usuario) {
		BeanUtils.copyProperties(usuario, this);
	}

}
