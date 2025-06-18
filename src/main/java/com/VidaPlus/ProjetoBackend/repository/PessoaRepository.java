package com.VidaPlus.ProjetoBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaEntity, Long> {
	Optional<PessoaEntity> findByUsuario(UsuarioEntity usuario);
	
}
