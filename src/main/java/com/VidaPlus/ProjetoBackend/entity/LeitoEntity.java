package com.VidaPlus.ProjetoBackend.entity;

import java.time.LocalDateTime;

import com.VidaPlus.ProjetoBackend.entity.enums.StatusLeito;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vps_leito")
@Setter
@Getter
public class LeitoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identificador; 

    @Enumerated(EnumType.STRING)
    private StatusLeito status; 

    @ManyToOne(optional = false)
    @JoinColumn(name = "hospital_id")
    private HospitalEntity hospital;

    @OneToOne
    @JoinColumn(name = "paciente_id", unique = true)
    private PessoaEntity paciente; 

    @Column(name = "data_ocupacao")
    private LocalDateTime dataOcupacao;

    @Column(name = "data_liberacao")
    private LocalDateTime dataLiberacao;
    
}
