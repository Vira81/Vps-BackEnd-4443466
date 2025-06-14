package com.VidaPlus.ProjetoBackend.auditoria;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class AuditoriaAspect {

	@Autowired
	private AuditoriaLogRepository auditoriaLogRepository;

	/**
	 * Cria logs de auditoria de todas as ações realizadas no servie.
	 * Exceto loadUserByUsename, evitando registros "duplicados" 
	 */
	@Before("within(com.VidaPlus.ProjetoBackend.service..*) && !execution(* com.VidaPlus.ProjetoBackend.service.UserDetailServiceImpl.loadUserByUsername(..))")

	public void logActivity(JoinPoint joinPoint) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (authentication != null) ? authentication.getName() : "Usuario_não_logado";

		LogAuditoria log = new LogAuditoria();
		log.setUsername(username);
		log.setAction(joinPoint.getSignature().getName());
		log.setTimestamp(new Date());
		log.setResource(joinPoint.getSignature().getDeclaringTypeName());
		log.setDetails(Arrays.toString(joinPoint.getArgs()));

		auditoriaLogRepository.save(log);
	}

}