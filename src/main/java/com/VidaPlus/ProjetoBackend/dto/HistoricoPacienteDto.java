package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HistoricoPacienteDto {
    private Long id;
    private LocalDate dataUltimaAtualizacao;
    private List<NovoHistoricoPacienteDto> entradas;
}