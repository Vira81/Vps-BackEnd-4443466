package com.VidaPlus.ProjetoBackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
	boolean existsByEmail(String email);

	Optional<UsuarioEntity> findByEmail(String email);
	
	/**
	 * Listar todos os usuarios.
	 * Query é necessario para carregar corretamente (o Spring nem inicia)
	 * fetch = FetchType.EAGER não funciounou
	 */
	@Query("""
		    SELECT u FROM UsuarioEntity u
		    LEFT JOIN FETCH u.pessoa p
		    LEFT JOIN FETCH p.profissionalSaude
		    """)
		List<UsuarioEntity> findAllSaida();
}
