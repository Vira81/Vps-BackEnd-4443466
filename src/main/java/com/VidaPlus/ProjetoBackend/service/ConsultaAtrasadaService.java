package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;
import com.VidaPlus.ProjetoBackend.repository.ConsultaRepository;

import jakarta.transaction.Transactional;

@Service
public class ConsultaAtrasadaService {
	/**
	 * Marca consultas 
	 */
    @Autowired
    private ConsultaRepository consultaRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // todo dia às 00:00
    //@Scheduled(fixedRate = 300000) // 5 minutos
    public void atualizarConsultasAtrasadas() {
    	final Logger log = LoggerFactory.getLogger(ConsultaAtrasadaService.class);
    	
    	// Faz uma lista com todas as consultas atrasadas e marcadas como Agendada
    	LocalDate hoje = LocalDate.now();
        List<ConsultaEntity> consultas = consultaRepository.findByStatusConsultaAndDiaBefore(ConsultaStatus.AGENDADA, hoje);

        // Marca a lista de consultas como Expirada 
        for (ConsultaEntity consulta : consultas) {
            consulta.setStatusConsulta(ConsultaStatus.EXPIRADA);
        }
        
        log.info("Serviço de atualização de consultas realizado com sucesso.");
        consultaRepository.saveAll(consultas);
    }
}