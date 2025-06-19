package com.VidaPlus.ProjetoBackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;

@Repository
public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaudeEntity, Long> {
    boolean existsByUsuarioId(Long usuarioId);
    Optional<ProfissionalSaudeEntity> findByUsuarioId(Long usuarioId);
    
    List<ProfissionalSaudeEntity> findByEspecialidade(EspecialidadeSaude especialidade);
}
