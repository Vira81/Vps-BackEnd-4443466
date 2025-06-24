package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDateTime;

import com.VidaPlus.ProjetoBackend.entity.enums.StatusTeleconsulta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeleconsultaSaidaDto {
    private Long id;
    private Long consultaId;
    private String linkConsulta;
    private StatusTeleconsulta status;
    private LocalDateTime dataGeracaoLink;

    public TeleconsultaSaidaDto(Long id, Long consultaId, String linkConsulta, StatusTeleconsulta status, LocalDateTime dataGeracaoLink) {
        this.id = id;
        this.consultaId = consultaId;
        this.linkConsulta = linkConsulta;
        this.status = status;
        this.dataGeracaoLink = dataGeracaoLink;
    }
}
