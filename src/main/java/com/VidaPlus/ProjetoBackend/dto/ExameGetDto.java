package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDate;

import com.VidaPlus.ProjetoBackend.entity.ExameEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExameGetDto {

    private Long id;
    private LocalDate dia;
    
    private String status;

    private String nomePaciente;
    private String nomeProfissional;
    private String nomeHospital;
    
    private Long consultaId;

    public ExameGetDto(ExameEntity exame) {
        this.id = exame.getId();
        this.dia = exame.getDia();
        this.status = exame.getStatusExame().toString();

        this.nomePaciente = exame.getPaciente().getNome();
        this.nomeHospital = exame.getHospital().getNome();

        if (exame.getProfissional() != null) {
        	this.nomeProfissional = exame.getProfissional().getPessoa().getNome();
        }
        
        if (exame.getConsulta() != null) {
        	this.consultaId = exame.getConsulta().getId();
        }
      
    }

}