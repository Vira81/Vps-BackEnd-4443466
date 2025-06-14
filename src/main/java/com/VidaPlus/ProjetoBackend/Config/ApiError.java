package com.VidaPlus.ProjetoBackend.Config;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record ApiError(LocalDateTime timestamp,

        Integer code,

        String status,

        List<String> errors) {
	

}
