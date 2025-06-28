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
     * 
     * GET http://localhost:8080/audit-logs
     */
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<LogAuditoria> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }
}