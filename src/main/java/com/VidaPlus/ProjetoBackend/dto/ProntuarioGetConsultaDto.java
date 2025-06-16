package com.VidaPlus.ProjetoBackend.dto;

import com.VidaPlus.ProjetoBackend.entity.ProntuarioEntity;

import lombok.Getter;

@Getter
public class ProntuarioGetConsultaDto {
    
    private String diagnostico;
    private String observacao;
   

    public ProntuarioGetConsultaDto(ProntuarioEntity prontuario) {
        
        this.diagnostico = prontuario.getDiagnostico();
        this.observacao = prontuario.getObservacao();
        
    }

}