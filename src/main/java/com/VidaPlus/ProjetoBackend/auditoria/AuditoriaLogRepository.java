package com.VidaPlus.ProjetoBackend.auditoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaLogRepository extends JpaRepository<LogAuditoria, Long> {
}