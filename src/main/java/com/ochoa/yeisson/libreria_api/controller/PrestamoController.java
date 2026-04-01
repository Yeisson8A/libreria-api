package com.ochoa.yeisson.libreria_api.controller;

import com.ochoa.yeisson.libreria_api.dto.CrearPrestamoRequest;
import com.ochoa.yeisson.libreria_api.dto.PrestamoDTO;
import com.ochoa.yeisson.libreria_api.response.ApiResponse;
import com.ochoa.yeisson.libreria_api.service.PrestamoService;
import com.ochoa.yeisson.libreria_api.util.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
public class PrestamoController {
    private final PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<ApiResponse<PrestamoDTO>> prestarLibro(
            @Valid @RequestBody CrearPrestamoRequest request) {

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        prestamoService.prestarLibro(
                                request.getLibroId(),
                                request.getUsuarioId()
                        ),
                        "Préstamo realizado correctamente"
                )
        );
    }

    @PutMapping("/devolver/{id}")
    public ResponseEntity<ApiResponse<PrestamoDTO>> devolverLibro(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        prestamoService.devolverLibro(id),
                        "Libro devuelto correctamente"
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PrestamoDTO>>> listarPrestamos() {

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        prestamoService.listarPrestamos(),
                        "Lista de préstamos"
                )
        );
    }
}
