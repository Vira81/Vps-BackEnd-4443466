// UsuarioPerfilUpdateDto.java
package com.VidaPlus.ProjetoBackend.dto;

import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioPerfilDto {
    private PerfilUsuario perfil;
    private StatusUsuario status;
}
