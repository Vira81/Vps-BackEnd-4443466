package com.VidaPlus.ProjetoBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.HistoricoPacienteEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;

@Repository
public interface HistoricoPacienteRepository extends JpaRepository<HistoricoPacienteEntity, Long> {
	Optional<HistoricoPacienteEntity> findByPaciente(PessoaEntity paciente);
}
