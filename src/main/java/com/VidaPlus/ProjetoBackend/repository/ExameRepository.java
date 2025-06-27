package com.VidaPlus.ProjetoBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.ExameEntity;

@Repository
public interface ExameRepository extends JpaRepository<ExameEntity, Long> {
	
}