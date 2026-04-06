package com.ochoa.yeisson.libreria_api.exception.support;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TestRequest {
    @NotNull(message = "campo es obligatorio")
    private String campo;
}
