package com.VidaPlus.ProjetoBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;

@Repository
public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaudeEntity, Long> {
    boolean existsByUsuarioId(Long usuarioId);
    Optional<ProfissionalSaudeEntity> findByUsuarioId(Long usuarioId);
}
