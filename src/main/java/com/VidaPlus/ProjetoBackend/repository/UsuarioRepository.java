package com.VidaPlus.ProjetoBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
	boolean existsByEmail(String email);

	Optional<UsuarioEntity> findByEmail(String email);
}
