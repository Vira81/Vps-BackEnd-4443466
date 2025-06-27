package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDate;

import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;
import com.VidaPlus.ProjetoBackend.entity.enums.TipoExame;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamePacienteDto {

	private Long pacienteId;
	@NotNull(message = "É necessário informar o Hospital.")
	private Long hospitalId;

	@NotNull(message = "É necessário informar o dia desejado.")
	private LocalDate dia;
	
	@NotNull(message = "Informe o exame desejado.")
	private TipoExame tipoExame; 
	
	private ConsultaStatus statusExame; 
}