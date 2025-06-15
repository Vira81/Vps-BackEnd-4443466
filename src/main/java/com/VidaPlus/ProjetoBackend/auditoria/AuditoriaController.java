package com.VidaPlus.ProjetoBackend.auditoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit-logs")
public class AuditoriaController {

    @Autowired
    private AuditoriaLogRepository auditLogRepository;

    /**
     * Visualiza os logs de auditoria
     * como o Service não é usado, essa ação não aparece no log
     * TODO: Verificar se isso ocorre em outras partes
     */
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<LogAuditoria> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }
}