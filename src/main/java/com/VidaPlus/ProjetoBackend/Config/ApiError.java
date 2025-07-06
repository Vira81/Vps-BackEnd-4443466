package com.VidaPlus.ProjetoBackend.Config;
/**
 * public record: Funciona normalmente, pórem apresentou erro em testes
 * usando um pc diferente. Usando class no momento para maior compatibilidade 
 * com outras versões.
 * 
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
@Builder
public record ApiError(LocalDateTime timestamp,
        Integer code,
        String status,
        List<String> errors) {
}
*/
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ApiError {
    private LocalDateTime timestamp;
    private Integer code;
    private String status;
    private List<String> errors;
}
