package com.ochoa.yeisson.libreria_api.controller;

import com.ochoa.yeisson.libreria_api.dto.CrearLibroRequest;
import com.ochoa.yeisson.libreria_api.dto.LibroDTO;
import com.ochoa.yeisson.libreria_api.response.ApiResponse;
import com.ochoa.yeisson.libreria_api.service.LibroService;
import com.ochoa.yeisson.libreria_api.util.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {
    private final LibroService libroService;

    @PostMapping
    public ResponseEntity<ApiResponse<LibroDTO>> crearLibro(
            @Valid @RequestBody CrearLibroRequest request) {
        return ResponseEntity.ok(
                ResponseBuilder.success(
                        libroService.crearLibro(request),
                        "Libro creado correctamente"
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LibroDTO>>> listarLibros() {
        return ResponseEntity.ok(
                ResponseBuilder.success(
                        libroService.listarLibros(),
                        "Lista de libros"
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LibroDTO>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseBuilder.success(
                        libroService.obtenerPorId(id),
                        "Libro encontrado"
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LibroDTO>> actualizarLibro(
            @PathVariable Long id,
            @Valid @RequestBody CrearLibroRequest request) {
        return ResponseEntity.ok(
                ResponseBuilder.success(
                        libroService.actualizarLibro(id, request),
                        "Libro actualizado correctamente"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);

        return ResponseEntity.ok(
                ResponseBuilder.success(null, "Libro eliminado correctamente")
        );
    }
}
