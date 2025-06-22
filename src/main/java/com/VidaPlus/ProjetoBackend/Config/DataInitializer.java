package com.VidaPlus.ProjetoBackend.Config;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;
import com.VidaPlus.ProjetoBackend.repository.ConsultaRepository;
import com.VidaPlus.ProjetoBackend.repository.HospitalRepository;
import com.VidaPlus.ProjetoBackend.repository.PessoaRepository;
import com.VidaPlus.ProjetoBackend.repository.ProfissionalSaudeRepository;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

/**
 * Inicia dados no banco
 */
//@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UsuarioRepository usuarioRepository,
            PessoaRepository pessoaRepository,
            HospitalRepository hospitalRepository,
            ProfissionalSaudeRepository profissionalRepository,
            ConsultaRepository consultaRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // Usuário Admin
            UsuarioEntity admin = UsuarioEntity.builder()
                    .email("admin@vida.com")
                    .senhaHash(passwordEncoder.encode("admin"))
                    .perfil(PerfilUsuario.ADMIN)
                    .status(StatusUsuario.ATIVO)
                    .pessoa(new PessoaEntity())
                    .build();
            usuarioRepository.save(admin);

            Optional<PessoaEntity> pessoaOptional = pessoaRepository.findById(1L);

            // "PUT" dos dados pessoais
            if (pessoaOptional.isPresent()) {
                PessoaEntity pessoa = pessoaOptional.get();
                pessoa.setNome("Admin");
                pessoa.setCpf("123.456.789-11");
                pessoa.setDataNascimento(LocalDate.of(1990, 5, 20));
                pessoa.setTelefone("(11) 99999-0000");

                pessoaRepository.save(pessoa);
            }

    
            // Hospital
            HospitalEntity hospital = HospitalEntity.builder()
                    .nome("Hospital Vida")
                    .build();
            hospitalRepository.save(hospital);
            
            HospitalEntity hospital2 = HospitalEntity.builder()
                    .nome("Hospital Viva Melhor")
                    .build();
            hospitalRepository.save(hospital2);

            HospitalEntity hospital3 = HospitalEntity.builder()
                    .nome("Hospital Centro")
                    .build();
            hospitalRepository.save(hospital3);
            
            // Profissional Saude
            UsuarioEntity profissionalUser = UsuarioEntity.builder()
                    .email("prof@vida.com")
                    .senhaHash(passwordEncoder.encode("prof"))
                    .perfil(PerfilUsuario.PROFISSIONAL)
                    .status(StatusUsuario.ATIVO)
                    .pessoa(new PessoaEntity())
                    .build();
            usuarioRepository.save(profissionalUser);

            Optional<PessoaEntity> pessoaOptional2 = pessoaRepository.findById(2L);

            // "PUT" dos dados pessoais
            if (pessoaOptional2.isPresent()) {
                PessoaEntity pessoaF = pessoaOptional2.get();
                pessoaF.setNome("Medico saude");
                pessoaF.setCpf("123.456.789-22");
                pessoaF.setDataNascimento(LocalDate.of(1990, 5, 20));
                pessoaF.setTelefone("(11) 99999-0000");

                pessoaRepository.save(pessoaF);
            }

           // ProfissionalSaudeEntity profissional = ProfissionalSaudeEntity.builder()
           //         .usuario(profissionalUser)
           //         .pessoa(pessoaF)
           //         .especialidade(EspecialidadeSaude.CARDIOLOGIA)
           //         .crm("CRM-SP-123456")
           //         .build();
           // profissionalRepository.save(profissional);

            // Paciente
            UsuarioEntity pacienteUser = UsuarioEntity.builder()
                    .email("paciente@vida.com")
                    .senhaHash(passwordEncoder.encode("pac"))
                    .perfil(PerfilUsuario.PACIENTE)
                    .status(StatusUsuario.ATIVO)
                    .pessoa(new PessoaEntity())
                    .build();
            usuarioRepository.save(pacienteUser);

            Optional<PessoaEntity> pessoaOptional3 = pessoaRepository.findById(3L);

            // "PUT" dos dados pessoais
            if (pessoaOptional3.isPresent()) {
                PessoaEntity pessoa = pessoaOptional3.get();
                pessoa.setNome("Paciente Saude");
                pessoa.setCpf("123.456.789-00");
                pessoa.setDataNascimento(LocalDate.of(1990, 5, 20));
                pessoa.setTelefone("(11) 99999-0000");

                pessoaRepository.save(pessoa);
            }


           
        };
    }
}
