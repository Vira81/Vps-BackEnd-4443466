package com.VidaPlus.ProjetoBackend.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.VidaPlus.ProjetoBackend.dto.ProntuarioDto;
import com.VidaPlus.ProjetoBackend.dto.TeleconsultaDto;
import com.VidaPlus.ProjetoBackend.dto.TeleconsultaSaidaDto;
import com.VidaPlus.ProjetoBackend.entity.ConsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.TeleconsultaEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.ConsultaStatus;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusTeleconsulta;
import com.VidaPlus.ProjetoBackend.repository.ConsultaRepository;
import com.VidaPlus.ProjetoBackend.repository.ProfissionalSaudeRepository;
import com.VidaPlus.ProjetoBackend.repository.TeleconsultaRepository;

@Service
public class TeleconsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private TeleconsultaRepository teleconsultaRepository;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    @Autowired
    private ProntuarioService prontuarioService;
    
    @Autowired
    private HistoricoPacienteService historicoPacienteService;

    @Autowired
    private ExisteService existe;
    
    public TeleconsultaSaidaDto agendarTeleconsulta(TeleconsultaDto dto) {

        // Paciente solicitando telemedicina e profissional solocitado
    	UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();
        ProfissionalSaudeEntity profissional = existe.profissionalSaude(dto.getProfissionalId());
        HospitalEntity hospitalVirtual = existe.hospitalVirtual(4L);
        
        // Consulta comum 
        ConsultaEntity consulta = new ConsultaEntity();
        consulta.setDia(dto.getDia());
        // placeholder
        consulta.setHora(LocalTime.now());
        consulta.setHospital(hospitalVirtual);
        consulta.setPaciente(usuario.getPessoa());
        consulta.setProfissional(profissional);
        consulta.setStatusConsulta(ConsultaStatus.AGENDADA);
        consulta.setTeleconsulta(true);

        consultaRepository.save(consulta);

        // Teleconsulta
        TeleconsultaEntity teleconsulta = new TeleconsultaEntity();
        teleconsulta.setConsulta(consulta);
        teleconsulta.setStatus(StatusTeleconsulta.AGENDADA);

        // link usado na consulta
        String sala = UUID.randomUUID().toString();
        String link = "http://localhost:8080/teleconsulta/sala/" + sala;
        teleconsulta.setLinkConsulta(link);
        teleconsulta.setSalaId(sala);
        teleconsulta.setDataGeracaoLink(LocalDateTime.now());

        teleconsultaRepository.save(teleconsulta);

        return saida(teleconsulta);
    }

    
    private TeleconsultaSaidaDto saida(TeleconsultaEntity tele) {
        return new TeleconsultaSaidaDto(
            tele.getId(),
            tele.getConsulta().getId(),
            tele.getLinkConsulta(),
            tele.getStatus(),
            tele.getDataGeracaoLink()
        );
    }
    
    public TeleconsultaSaidaDto realizarTeleconsulta(String salaId) {
        
    	// Identificação
    	TeleconsultaEntity teleconsulta = existe.teleconsulta(salaId);
        UsuarioEntity usuario = usuarioLogadoService.getUsuarioLogado();

        // Status
        if (teleconsulta.getStatus() != StatusTeleconsulta.AGENDADA) {
            throw new IllegalStateException("A teleconsulta já foi iniciada ou encerrada.");
        }
        
        // Paciente é dono da consulta
        if (!teleconsulta.getConsulta().getPaciente().getId().equals(usuario.getId())) {
        	throw new IllegalStateException("Como vc chegou aqui???. Verifique o Token.");
        }

        /**
         * >>>>> PLACEHOLDER TODO:
         * Quando o usuario entrar na teleconsulta, marca como em andamento.
         * O profissional finaliza a consulta, gera o prontuario, prescrição na conta dele.
         * 
         * No momento tudo isso é gerado aqui com o usuario.
         */
        
        teleconsulta.setStatus(StatusTeleconsulta.FINALIZADA);
        teleconsultaRepository.save(teleconsulta);
        
        // Atualiza a consulta
        teleconsulta.getConsulta().setStatusConsulta(ConsultaStatus.REALIZADA);
        teleconsulta.getConsulta().setDataRealizada(LocalDateTime.now());
        
     // Gera o prontuario
     	ProntuarioDto dto = new ProntuarioDto(); 
     	dto.setDiagnostico("Dolor "+salaId);	
        prontuarioService.gerarProntuario(teleconsulta.getConsulta(), dto);

     		// Salva no histórico do paciente
     		String descricao = String.format("TeleConsulta realizada em %s com Dr(a). %s.",
     				teleconsulta.getConsulta().getDataRealizada().toLocalDate(), teleconsulta.getConsulta().getProfissional().getPessoa().getNome());

     		historicoPacienteService.registrarEntradaConsulta(teleconsulta.getConsulta().getPaciente(), teleconsulta.getConsulta().getProfissional(), descricao,
     				teleconsulta.getConsulta());


        return saida(teleconsulta);
    }

}
