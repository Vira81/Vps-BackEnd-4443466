package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeleconsultaDto {
	private Long profissionalId;
    private LocalDate dia;
    
}
