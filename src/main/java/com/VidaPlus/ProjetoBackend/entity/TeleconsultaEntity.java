package com.VidaPlus.ProjetoBackend.entity;

import java.time.LocalDateTime;

import com.VidaPlus.ProjetoBackend.entity.enums.StatusTeleconsulta;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "vps_teleconsulta")
public class TeleconsultaEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    private ConsultaEntity consulta;

    private String linkConsulta;
    
    private String salaId;

    private LocalDateTime dataGeracaoLink;

    @Enumerated(EnumType.STRING)
    private StatusTeleconsulta status; 


}
