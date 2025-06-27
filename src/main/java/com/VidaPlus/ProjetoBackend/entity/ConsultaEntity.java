package com.VidaPlus.ProjetoBackend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.BeanUtils;

import com.VidaPlus.ProjetoBackend.dto.ConsultaDto;
import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
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
@Table(name = "vps_consulta")
@Builder
/**
 * Responsavel por criar uma consulta Consulta -- Paciente, Profissional,
 * Hospital
 * 
 */

public class ConsultaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private LocalDate dia;

	@Column(nullable = false)
	private LocalTime hora;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCriacaoConsulta;

	@Column
	private Double valor;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ConsultaStatus statusConsulta;

	@ManyToOne(optional = false)
	@JoinColumn(name = "profissional_id", nullable = false)
	private ProfissionalSaudeEntity profissional;

	@ManyToOne(optional = false)
	@JoinColumn(name = "paciente_id", nullable = false)
	private PessoaEntity paciente;

	/**
	 * Essa consulta é presencial, consulta online será realizada pela telemedicina
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "hospital_id", nullable = false)
	private HospitalEntity hospital;
	
	// true , se for telemedicina
	@Column (nullable = false)
	@Builder.Default
	private Boolean teleconsulta = false;

	@OneToOne(mappedBy = "consulta")
	private TeleconsultaEntity teleconsultaEn;

	private LocalDateTime dataRealizada;

	@OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL)
	@JsonIgnore
	private PrescricaoEntity prescricao;

	@OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL)
	@JsonIgnore
	private ProntuarioEntity prontuario;

	@OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL)
	@JsonIgnore
	private ExameEntity exame;
	
	public ConsultaEntity(ConsultaDto usuario) {
		BeanUtils.copyProperties(usuario, this);
	}

}
