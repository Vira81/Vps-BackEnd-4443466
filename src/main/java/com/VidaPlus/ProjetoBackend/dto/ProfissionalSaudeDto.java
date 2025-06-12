package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDateTime;

import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;

import lombok.Data;

@Data
public class ProfissionalSaudeDto {
    private Long Id;
	private Long usuarioId;
    private Long pessoaId;
    private EspecialidadeSaude especialidade;
    private String crm;
    private LocalDateTime dataCriacaoProfissional;
    private String criadoPor;
}