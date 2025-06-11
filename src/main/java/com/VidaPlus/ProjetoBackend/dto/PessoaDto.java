package com.VidaPlus.ProjetoBackend.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaDto {
    private String nome;
    private String cpf;
    private String telefone;
    private LocalDate dataNascimento;

}
