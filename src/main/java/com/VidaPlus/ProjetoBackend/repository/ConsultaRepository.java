package com.VidaPlus.ProjetoBackend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaEntity, Long> {
	List<ConsultaEntity> findByStatusConsultaAndDiaBefore(ConsultaStatus status, LocalDate dia);

}
