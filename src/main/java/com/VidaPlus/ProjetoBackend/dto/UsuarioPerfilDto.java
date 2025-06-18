package com.VidaPlus.ProjetoBackend.dto;

import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioPerfilDto {
    @NotBlank(message = "O cpf não pode estar vazio ")
	private String cpf;
	private PerfilUsuario perfil;
    private StatusUsuario status;
}
