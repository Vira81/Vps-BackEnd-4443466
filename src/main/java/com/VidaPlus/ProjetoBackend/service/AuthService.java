package com.VidaPlus.ProjetoBackend.service;


import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.AccessDto;
import com.VidaPlus.ProjetoBackend.dto.AuthenticationDto;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;
import com.VidaPlus.ProjetoBackend.security.JwtUtils;


@Service
public class AuthService {
	private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/**
	 * Efetua o login e gera o token de acesso
	 */
	public AccessDto login(AuthenticationDto authDto) {
	    try {
	        UsernamePasswordAuthenticationToken userAuth =
	                new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());

	        Authentication authentication = authenticationManager.authenticate(userAuth);

	        UserDetailsImpl userAuthenticate = (UserDetailsImpl) authentication.getPrincipal();
	        UsuarioEntity usuario = userAuthenticate.getUsuario();

	        // Usuario Inativo e Pendente não pode acessar
	        if (usuario.getStatus() == StatusUsuario.INATIVO) {
	        	throw new AccessDeniedException("Essa conta está desativada.");
	        }
	        
	        if (usuario.getStatus() == StatusUsuario.PENDENTE) {
	        	throw new AccessDeniedException("Ative sua conta. \n"
	        			+ "PUT http://localhost:8080/usuarios/cadastro/"+usuario.getCod());
	        }
	        
	        // Atualiza o último acesso
	        usuario.setUltimoAcesso(LocalDateTime.now());
	        usuarioRepository.save(usuario);
	        String token = jwtUtils.generateTokenFromUserDetailsImpl(userAuthenticate);

	        return new AccessDto(token);
	    } catch (BadCredentialsException e) {
	        logger.warn("Credenciais inválidas para o usuário: {}", authDto.getUsername());
	        throw new AccessDeniedException("Login ou senha inválidos");
	    } 
	    }
	}
	

