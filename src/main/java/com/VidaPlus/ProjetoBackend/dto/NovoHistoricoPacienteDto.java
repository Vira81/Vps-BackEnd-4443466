package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDate;

import com.VidaPlus.ProjetoBackend.entity.enums.TipoEspecialidadeSaude;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NovoHistoricoPacienteDto {
    private LocalDate dataEntrada;
    private String descricao;
    private String nomeProfissional;
    private TipoEspecialidadeSaude tipo;
    
    private String diagnostico;
    private String observacao;
    private String medicacao;
    private String posologia;
}