package com.VidaPlus.ProjetoBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;

public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaudeEntity, Long> {
    boolean existsByUsuarioId(Long usuarioId);
}
