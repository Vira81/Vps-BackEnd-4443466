package com.VidaPlus.ProjetoBackend.entity;

import java.time.LocalDate;

import com.VidaPlus.ProjetoBackend.entity.enums.TipoEspecialidadeSaude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vps_novo_historico_paciente")
public class NovoHistoricoPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private HistoricoPaciente historicoPaciente;

    @Column(length = 1000)
    private String descricao;

    private LocalDate dataEntrada;

    @ManyToOne
    private ProfissionalSaudeEntity profissionalResponsavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id")
    private ConsultaEntity consulta;
    
    @Enumerated(EnumType.STRING)
    private TipoEspecialidadeSaude tipo; 
}
