package com.VidaPlus.ProjetoBackend.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.AcessDto;
import com.VidaPlus.ProjetoBackend.dto.AuthenticationDto;
import com.VidaPlus.ProjetoBackend.security.JwtUtils;


@Service
public class AuthService {
	private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	public AcessDto login(AuthenticationDto authDto) {
	    try {
	        UsernamePasswordAuthenticationToken userAuth =
	                new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());

	        Authentication authentication = authenticationManager.authenticate(userAuth);

	        UserDetailsImpl userAuthenticate = (UserDetailsImpl) authentication.getPrincipal();

	        String token = jwtUtils.generateTokenFromUserDetailsImpl(userAuthenticate);

	        return new AcessDto(token);
	    } catch (BadCredentialsException e) {
	        logger.warn("Credenciais inválidas para o usuário: {}", authDto.getUsername());
	        throw new RuntimeException("Login ou senha inválidos");
	    } catch (Exception e) {
	        logger.error("Erro ao autenticar: {}", authDto.getUsername(), e);
	        throw new RuntimeException("Erro interno na autenticação");
	    }
	}
	}

