package com.VidaPlus.ProjetoBackend.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;

@SuppressWarnings("serial")
public class UserDetailsImpl implements UserDetails {

    private final UsuarioEntity usuario;

    public UserDetailsImpl(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    /**
     * Atribui as permissões de acesso usando o Perfil
     * 
     * @PreAuthorize("hasRole('ADMIN')")
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name())
        );
    }

    @Override
    public String getPassword() {
        return usuario.getSenhaHash();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }
}
