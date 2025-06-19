package com.VidaPlus.ProjetoBackend.dto;

import java.time.DayOfWeek;
import java.util.List;

import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuscaEspecialidadeDto {

	private String medico;

	private EspecialidadeSaude especialidade;
	private String crm;

	private List<String> hospitais;
	private List<DayOfWeek> diasAtendimento;

}