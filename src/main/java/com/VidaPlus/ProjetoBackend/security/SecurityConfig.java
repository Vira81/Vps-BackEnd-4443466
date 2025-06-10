package com.VidaPlus.ProjetoBackend.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
/**
 * paginas que podem ser acesadas sem autenticaçao,
 * SOMENTE PARA TESTES
 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/usuarios/**").permitAll() // libera o CRUD
                .anyRequest().authenticated()
            .and()
            .httpBasic();

        return http.build();
    }

/**
 * 
 * @return senha criptografada
 */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}