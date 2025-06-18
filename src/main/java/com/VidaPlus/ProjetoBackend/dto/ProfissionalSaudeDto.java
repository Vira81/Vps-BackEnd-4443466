package com.VidaPlus.ProjetoBackend.dto;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.stream.Collectors;

import com.VidaPlus.ProjetoBackend.entity.HospitalEntity;
import com.VidaPlus.ProjetoBackend.entity.ProfissionalSaudeEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.EspecialidadeSaude;
import com.VidaPlus.ProjetoBackend.entity.enums.FuncaoSaude;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfissionalSaudeDto {
    private String cpf;
	
    private EspecialidadeSaude especialidade;
    private FuncaoSaude funcao;
    private String crm;
  
    private Set<DayOfWeek> diasTrabalho;
    private Set<Long> hospitaisIds;
    
    public ProfissionalSaudeDto() {
    }


    public ProfissionalSaudeDto(ProfissionalSaudeEntity entity) {
        if (entity.getPessoa() != null) {
            this.cpf = entity.getPessoa().getCpf();
        }
        this.crm = entity.getCrm();
        this.especialidade = entity.getEspecialidade();
        this.funcao = entity.getFuncao();

        if (entity.getDiasTrabalho() != null) {
            this.diasTrabalho = entity.getDiasTrabalho();
        }

        if (entity.getHospitais() != null) {
            this.hospitaisIds = entity.getHospitais().stream()
                .map(HospitalEntity::getId)
                .collect(Collectors.toSet());
        }
    }
}
