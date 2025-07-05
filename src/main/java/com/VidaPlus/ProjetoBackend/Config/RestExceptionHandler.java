package com.VidaPlus.ProjetoBackend.Config;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.VidaPlus.ProjetoBackend.exception.AlteracaoIndevida;
import com.VidaPlus.ProjetoBackend.exception.EmailJaCadastradoException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class RestExceptionHandler {

/**
 * Tratamento de erros
 * TODO: Criar mais mensagens personalizadas
 * Ja criada: Erros de cadastro, email/senha invalido, ENUM
 * 
 */
	//Se um usuario tentar alterar os dados pessoais de outra pessoa
	// TODO: a mensagem ainda nao mudou 403 Generico
	@ExceptionHandler(AlteracaoIndevida.class)
    public ResponseEntity<ApiError> handleAlteracaoIndevida(AlteracaoIndevida ex) {
        ApiError apiError = criarErro(HttpStatus.FORBIDDEN, "Acesso negado: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }
	
	@ExceptionHandler(EmailJaCadastradoException.class)
	public ResponseEntity<ApiError> handleEmailJaCadastrado(EmailJaCadastradoException ex) {
	    ApiError apiError = criarErro(HttpStatus.CONFLICT, ex.getMessage());
	    return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}
	

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
	    Throwable causa = ex.getCause();

	    // Enum inválido
	    if (causa instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
	        Object[] valoresValidos = ife.getTargetType().getEnumConstants();
	        String opcoes = String.join(", ",
	                Arrays.stream(valoresValidos).map(Object::toString).toList());

	        String nomeCampo = ife.getPath().isEmpty() 
	        	    ? "desconhecido"
	        	    : ife.getPath().get(ife.getPath().size() - 1).getFieldName();

	        	String mensagem = "Valor inválido para o campo '" + nomeCampo + 
	        	    "'. Valores esperados: [" + opcoes + "].";

	        ApiError apiError = criarErro(HttpStatus.BAD_REQUEST, mensagem);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	    }

	    ApiError apiError = criarErro(HttpStatus.BAD_REQUEST, "Erro de leitura no corpo da requisição: " + ex.getMessage());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleViolacaoIntegridade(DataIntegrityViolationException ex) {
        ApiError apiError = criarErro(HttpStatus.CONFLICT, "Violação de integridade referencial ou dados duplicados. Verifique o JSON.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        
    	if (ex.getMessage()=="Access Denied") {
        	ApiError apiError = criarErro(HttpStatus.FORBIDDEN, "Acesso negado: Você não tem permissão para acessar este recurso.");
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    	} 
        
        ApiError apiError = criarErro(HttpStatus.FORBIDDEN, "Acesso negado: "+ ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidacaoCampos(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getAllErrors().stream()
            .map(error -> {
                String campo = error instanceof FieldError fe ? fe.getField() : "Campo";
                String mensagem = error.getDefaultMessage();
                return campo + ": " + mensagem;
            })
            .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .errors(erros)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex) {
        ApiError apiError = criarErro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    private ApiError criarErro(HttpStatus status, String mensagem) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(status.value())
                .status(status.name())
                .errors(List.of(mensagem))
                .build();
    }
}
