package com.VidaPlus.ProjetoBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.ProntuarioEntity;

@Repository
public interface ProntuarioRepository extends JpaRepository<ProntuarioEntity, Long> {
}