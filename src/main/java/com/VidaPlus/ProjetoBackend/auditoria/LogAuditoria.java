package com.VidaPlus.ProjetoBackend.auditoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "auditoria")
@Getter
@Setter
public class LogAuditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; 
    @Column(nullable = false)
    private String action; 
    @Column(nullable = false)
    private Date timestamp; 
    @Column(nullable = false)
    private String resource; 
    @Column
    private String details; 

   
}