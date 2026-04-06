package com.ochoa.yeisson.libreria_api.exception.support;

import com.ochoa.yeisson.libreria_api.exception.BadRequestException;
import com.ochoa.yeisson.libreria_api.exception.BusinessException;
import com.ochoa.yeisson.libreria_api.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-exceptions")
public class TestExceptionController {
    @GetMapping("/not-found")
    public void notFound() {
        throw new ResourceNotFoundException("No encontrado");
    }

    @GetMapping("/bad-request")
    public void badRequest() {
        throw new BadRequestException("Petición inválida");
    }

    @GetMapping("/business")
    public void business() {
        throw new BusinessException("Error de negocio");
    }

    @GetMapping("/general")
    public void general() {
        throw new RuntimeException("Error genérico");
    }

    @PostMapping("/validation")
    public void validation(@Valid @RequestBody TestRequest request) {
    }
}
