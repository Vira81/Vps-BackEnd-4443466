package com.VidaPlus.ProjetoBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.HistoricoPaciente;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;

@Repository
public interface HistoricoPacienteRepository extends JpaRepository<HistoricoPaciente, Long> {
	Optional<HistoricoPaciente> findByPaciente(PessoaEntity paciente);
}
