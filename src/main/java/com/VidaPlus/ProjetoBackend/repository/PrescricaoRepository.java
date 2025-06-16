package com.VidaPlus.ProjetoBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.PrescricaoEntity;

@Repository
public interface PrescricaoRepository extends JpaRepository<PrescricaoEntity, Long> {
}