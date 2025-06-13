package com.VidaPlus.ProjetoBackend.entity;

import org.springframework.beans.BeanUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "vps_hospital")
/**
 * Classe Hospital, no momento ela é generica
 * TODO: Implementar essa classe para fazer a Administração hospitalar
 */
public class HospitalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String nome;

	public HospitalEntity(HospitalEntity usuario) {
		BeanUtils.copyProperties(usuario, this);
	}

}
