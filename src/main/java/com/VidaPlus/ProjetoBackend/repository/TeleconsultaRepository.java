package com.VidaPlus.ProjetoBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.TeleconsultaEntity;

@Repository
public interface TeleconsultaRepository extends JpaRepository<TeleconsultaEntity, Long> {
	Optional<TeleconsultaEntity> findBySalaId(String salaId);
}
