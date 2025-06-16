package com.VidaPlus.ProjetoBackend.dto;

import com.VidaPlus.ProjetoBackend.entity.PrescricaoEntity;

import lombok.Getter;

@Getter
public class PrescricaoGetConsultaDto {
    
    private String medicacao;
    private String posologia;

    public PrescricaoGetConsultaDto(PrescricaoEntity prescricao) {
    
        this.medicacao = prescricao.getMedicacao();
        this.posologia = prescricao.getPosologia();
   
    }

   
}