package com.VidaPlus.ProjetoBackend.dto;

import org.springframework.beans.BeanUtils;

import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;

import lombok.Data;

@Data
public class HospitalDto {
	private Long id;
    private String nome;

	public HospitalDto(HospitalEntity usuario) {
		BeanUtils.copyProperties(usuario, this);
	}
}
