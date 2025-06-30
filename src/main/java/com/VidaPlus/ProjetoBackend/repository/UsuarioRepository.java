package com.VidaPlus.ProjetoBackend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
	boolean existsByEmail(String email);
	
	boolean existsByCod(String cod);

	boolean existsByPessoaCpf(String cpf);
	
	Optional<UsuarioEntity> findByEmail(String email);
	
	Optional<UsuarioEntity> findByCod(String cod);

	List<UsuarioEntity> findByPessoaNomeContainingIgnoreCase(String nome);
	
	List<UsuarioEntity> findByStatusAndDataCriacaoBefore(StatusUsuario status, LocalDateTime tempo);
	
	Optional<UsuarioEntity> findByPessoaCpf(String cpf);


	/**
	 * Listar todos os usuarios. Query é necessario para carregar corretamente (o
	 * Spring nem inicia) fetch = FetchType.EAGER não funciounou
	 */
	@Query("""
			SELECT u FROM UsuarioEntity u
			LEFT JOIN FETCH u.pessoa p
			LEFT JOIN FETCH p.profissionalSaude
			""")
	List<UsuarioEntity> findAllSaida();
}
