package com.VidaPlus.ProjetoBackend.entity;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;
import com.VidaPlus.ProjetoBackend.entity.enums.FuncaoSaude;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vps_profissionalsaude")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfissionalSaudeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(optional = false)
	@JoinColumn(name = "usuario_id", nullable = false)
	private UsuarioEntity usuario;

	@OneToOne(optional = false)
	@JoinColumn(name = "pessoa_id", nullable = false)
	private PessoaEntity pessoa;

	@Enumerated(EnumType.STRING)
	//@Column(nullable = false)
	private FuncaoSaude funcao;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EspecialidadeSaude especialidade;

	@Column
	private String crm;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCriacaoProfissional;

	@ManyToMany
	@JoinTable(
	    name = "profissional_hospital",
	    joinColumns = @JoinColumn(name = "profissional_id"),
	    inverseJoinColumns = @JoinColumn(name = "hospital_id")
	)
	@Builder.Default
	private Set<HospitalEntity> hospitais = new HashSet<>();
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "vps_dias_trabalho", joinColumns = @JoinColumn(name = "profissional_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "dia_semana")
	private Set<DayOfWeek> diasTrabalho;
	
}
