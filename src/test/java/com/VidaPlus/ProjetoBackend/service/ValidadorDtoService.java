package com.VidaPlus.ProjetoBackend.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidadorDtoService {

    private final Validator validador;

    public <T> void dto(T objeto) {
        Set<ConstraintViolation<T>> erro = validador.validate(objeto);

        if (!erro.isEmpty()) {
            throw new ConstraintViolationException(erro);
        }
    }
}
