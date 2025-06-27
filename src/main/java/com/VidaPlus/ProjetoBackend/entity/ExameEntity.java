package com.VidaPlus.ProjetoBackend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "vps_exame")
@Builder

public class ExameEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private LocalDate dia;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCriacaoExame;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ConsultaStatus statusExame;

	@ManyToOne(optional = true)
	@JoinColumn(name = "profissional_id", nullable = true)
	private ProfissionalSaudeEntity profissional;

	@ManyToOne(optional = false)
	@JoinColumn(name = "paciente_id", nullable = false)
	private PessoaEntity paciente;

	@ManyToOne(optional = false)
	@JoinColumn(name = "hospital_id", nullable = false)
	private HospitalEntity hospital;

	private LocalDateTime dataRealizada;

}
