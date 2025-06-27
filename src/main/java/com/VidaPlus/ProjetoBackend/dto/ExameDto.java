package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDate;

import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExameDto {

	private Long profissionalId;

	private Long pacienteId;
	
	private Long consultaId;
	
	@NotNull(message = "É necessário informar o Hospital.")
	private Long hospitalId;

	@NotNull(message = "É necessário informar o dia desejado.")
	private LocalDate dia;
	
	private ConsultaStatus statusExame; 
}
