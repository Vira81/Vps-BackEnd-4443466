package com.VidaPlus.ProjetoBackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.LeitoEntity;

@Repository
public interface LeitoRepository extends JpaRepository<LeitoEntity, Long> {
	
	List<LeitoEntity> findByPacienteId(Long pacienteId);

	List<LeitoEntity> findByHospitalId(Long hospitalId);

}
