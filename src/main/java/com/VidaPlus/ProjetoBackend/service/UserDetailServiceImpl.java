package com.VidaPlus.ProjetoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    UsuarioEntity usuario = usuarioRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

	    return new UserDetailsImpl(usuario);
	}

}
