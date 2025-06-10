package com.VidaPlus.ProjetoBackend.entity.enums;
/**
 * Status do login do usuario
 * Pendente - Precisa de verificação via email, sms, etc
 * Ativo - Usuario verificado
 * Inativo - usuario desativado por alguma regra
 */

public enum StatusUsuario {
	ATIVO,
	INATIVO,
	PENDENTE

}
