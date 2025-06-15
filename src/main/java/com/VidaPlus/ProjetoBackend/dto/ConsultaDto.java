package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaDto {

	private Long profissionalId;

	private Long pacienteId;
	
	private Long hospitalId;

	private LocalDate dia;
	
	private LocalTime hora;
	
	private Double valor;
	
	private ConsultaStatus statusConsulta; 
}
