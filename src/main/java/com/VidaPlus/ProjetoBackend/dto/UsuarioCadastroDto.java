package com.VidaPlus.ProjetoBackend.dto;

import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Cadastro de usuario
 * Use email, senha, perfil, status no JSON
 * TODO: remover perfil, status.
 */
public class UsuarioCadastroDto {
    private String email;
    private String senha;
    private PerfilUsuario perfil;
    private StatusUsuario status;

}
