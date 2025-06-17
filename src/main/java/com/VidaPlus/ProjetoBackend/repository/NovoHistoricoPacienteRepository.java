package com.VidaPlus.ProjetoBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VidaPlus.ProjetoBackend.entity.NovoHistoricoPaciente;

@Repository
public interface NovoHistoricoPacienteRepository extends JpaRepository<NovoHistoricoPaciente, Long> {

}
