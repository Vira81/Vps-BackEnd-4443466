package com.VidaPlus.ProjetoBackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PrescricaoDto {
	
	@NotBlank(message = "Se a prescrição foi criada, o campo medicação é obrigatório.")
	private String medicacao;

	@NotBlank(message = "Posologia é obrigatória.")
	private String posologia;
	
}
