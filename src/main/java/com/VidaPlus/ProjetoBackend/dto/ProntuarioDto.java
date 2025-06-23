package com.VidaPlus.ProjetoBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProntuarioDto {
    
	@NotNull(message = "O diagnostico é obrigatorio.")
	@NotBlank(message = "O diagnostico não pode estar vazio.")
	private String diagnostico;
    
	private String observacao;
}
