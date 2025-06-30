package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioPendente {
	/**
	 * Tarefa agendada para marcar consultas atrasadas como expiradas.
	 */
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    //@Scheduled(cron = "0 0 0 * * *") // todo dia às 00:00
    @Scheduled(fixedRate = 60000) // 1 minuto
    public void limparUsuariosPendentes() {
    	final Logger log = LoggerFactory.getLogger(UsuarioPendente.class);
    	
    	// Lista com usuarios que fizeram cadastro e nao ativaram a conta em 15 minutos
    	LocalDateTime tempo = LocalDateTime.now().minusMinutes(15);
        List<UsuarioEntity> pendentes = usuarioRepository.findByStatusAndDataCriacaoBefore(StatusUsuario.PENDENTE, tempo);
        		
        // Exclui os usuarios
        for (UsuarioEntity usuario : pendentes) {
            usuarioRepository.delete(usuario);;
        }
        
        log.info("Serviço de atualização de usuarios realizado com sucesso.");
        
    }
}