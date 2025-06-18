package com.VidaPlus.ProjetoBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, Long> {
	Optional<HospitalEntity> findById(Long id);

}
