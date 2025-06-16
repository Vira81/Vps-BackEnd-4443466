package com.VidaPlus.ProjetoBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// Criação do prontuario
// Renomear para ProntuarioDto
public class RealizarConsultaDto {
    private String diagnostico;
    private String observacao;
}
