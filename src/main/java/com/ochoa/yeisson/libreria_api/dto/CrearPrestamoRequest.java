package com.ochoa.yeisson.libreria_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CrearPrestamoRequest {
    @NotNull(message = "El libroId es obligatorio")
    private Long libroId;

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;
}
