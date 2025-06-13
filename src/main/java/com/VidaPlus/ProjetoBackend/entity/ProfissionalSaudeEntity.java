package com.VidaPlus.ProjetoBackend.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.BeanUtils;

import com.VidaPlus.ProjetoBackend.dto.ProfissionalSaudeDto;
import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	@Column(nullable = false)
	private EspecialidadeSaude especialidade;

	@Column
	private String crm;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCriacaoProfissional;

	@Column
	private String criadoPor;

	public ProfissionalSaudeEntity(ProfissionalSaudeDto usuario) {
		BeanUtils.copyProperties(usuario, this);
	}
}
