package com.VidaPlus.ProjetoBackend.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vps_historico_paciente")
public class HistoricoPaciente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	private PessoaEntity paciente;

	private LocalDate dataUltimaAtualizacao;

	@OneToMany(mappedBy = "historicoPaciente", cascade = CascadeType.ALL)
	private List<NovoHistoricoPaciente> entradas = new ArrayList<>();
}
