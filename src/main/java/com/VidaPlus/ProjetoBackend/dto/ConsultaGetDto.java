package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultaGetDto {

    private Long id;
    private LocalDate dia;
    private LocalTime hora;
    private String status;

    private String nomePaciente;
    private String nomeProfissional;
    private String nomeHospital;

    private ProntuarioGetConsultaDto prontuario;
    private PrescricaoGetConsultaDto prescricao;
    private ExameGetConsultaDto exame;
    
    private String link;

    public ConsultaGetDto(ConsultaEntity consulta) {
        this.id = consulta.getId();
        this.dia = consulta.getDia();
        this.hora = consulta.getHora();
        this.status = consulta.getStatusConsulta().name();

        this.nomePaciente = consulta.getPaciente().getNome();
        this.nomeProfissional = consulta.getProfissional().getPessoa().getNome();
        this.nomeHospital = consulta.getHospital().getNome();

        if (consulta.getProntuario() != null) {
            this.prontuario = new ProntuarioGetConsultaDto(consulta.getProntuario());
        }

        if (consulta.getPrescricao() != null) {
            this.prescricao = new PrescricaoGetConsultaDto(consulta.getPrescricao());
        }
        
        if (consulta.getExame() != null) {
            this.exame = new ExameGetConsultaDto(consulta.getExame());
        }
        
        if (Boolean.TRUE.equals(consulta.getTeleconsulta())) {
            this.link = consulta.getTeleconsultaEn().getLinkConsulta();
        }
    }

}
