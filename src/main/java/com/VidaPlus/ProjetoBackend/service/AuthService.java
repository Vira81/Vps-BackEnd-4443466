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

import com.VidaPlus.ProjetoBackend.dto.AcessDto;
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
	
	public AcessDto login(AuthenticationDto authDto) {
	    try {
	        UsernamePasswordAuthenticationToken userAuth =
	                new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());

	        Authentication authentication = authenticationManager.authenticate(userAuth);

	        UserDetailsImpl userAuthenticate = (UserDetailsImpl) authentication.getPrincipal();
	        UsuarioEntity usuario = userAuthenticate.getUsuario();

	        // Usuario Inativo não pode acessar
	        // TODO: Controlar os outros status, quando a verificação por email estiver pronta
	        if (usuario.getStatus() == StatusUsuario.INATIVO) {
	        	throw new AccessDeniedException("Essa conta está desativada.");
	        }
	        
	        // Atualiza o último acesso
	        usuario.setUltimoAcesso(LocalDateTime.now());
	        usuarioRepository.save(usuario);
	        String token = jwtUtils.generateTokenFromUserDetailsImpl(userAuthenticate);

	        return new AcessDto(token);
	    } catch (BadCredentialsException e) {
	        logger.warn("Credenciais inválidas para o usuário: {}", authDto.getUsername());
	        throw new AccessDeniedException("Login ou senha inválidos");
	    } catch (Exception e) {
	        logger.error("Erro ao autenticar: {}", authDto.getUsername(), e);
	        throw new RuntimeException("Erro interno na autenticação " + e);
	    }
	}
	}

