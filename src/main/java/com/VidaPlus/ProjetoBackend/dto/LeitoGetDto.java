package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDateTime;

import com.VidaPlus.ProjetoBackend.entity.LeitoEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusLeito;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeitoGetDto {

	private String identificador;
	private StatusLeito status;

	private String nomePaciente;
	private String nomeHospital;

	private LocalDateTime dataOcupacao;
	private LocalDateTime dataLiberacao;

	public LeitoGetDto(LeitoEntity leito) {
		this.identificador = leito.getIdentificador();
		this.status = leito.getStatus();

		if (leito.getPaciente() != null) {
			this.nomePaciente = leito.getPaciente().getNome();
		}
		this.nomeHospital = leito.getHospital().getNome();

		this.dataLiberacao = leito.getDataLiberacao();
		this.dataOcupacao = leito.getDataOcupacao();

	}
}