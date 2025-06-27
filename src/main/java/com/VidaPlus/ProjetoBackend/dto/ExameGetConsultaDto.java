package com.VidaPlus.ProjetoBackend.dto;

import com.VidaPlus.ProjetoBackend.entity.ExameEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.TipoExame;

import lombok.Getter;

@Getter
public class ExameGetConsultaDto {
    
    private TipoExame tipo;
    private String linkExame;

    public ExameGetConsultaDto(ExameEntity exame) {
    
        this.tipo = exame.getTipoExame();
        this.linkExame = null;
   
    }}