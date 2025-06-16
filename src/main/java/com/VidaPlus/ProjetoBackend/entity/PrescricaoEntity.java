package com.VidaPlus.ProjetoBackend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Getter
@Setter
@Table(name = "vps_prescricao")
public class PrescricaoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "consulta_id", nullable = false, unique = true)
	private ConsultaEntity consulta;

	@ManyToOne
	@JoinColumn(name = "profissional_id", nullable = false)
	private ProfissionalSaudeEntity profissional;

	@ManyToOne
	@JoinColumn(name = "paciente_id", nullable = false)
	private PessoaEntity paciente;

	@CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
	/**
	 ** Hospital_id já esta na consulta (Como no prontuario)
	 **
	 * @ManyToOne
	 * @JoinColumn(name = "hospital_id", nullable = false) private HospitalEntity
	 *                  hospital;
	 **/
	@Column
	private String medicacao;

	@Column
	private String posologia;

	@Column
	private String hospitalNome;
}
