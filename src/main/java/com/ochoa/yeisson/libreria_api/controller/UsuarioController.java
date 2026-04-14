package com.ochoa.yeisson.libreria_api.controller;

import com.ochoa.yeisson.libreria_api.dto.CrearUsuarioRequest;
import com.ochoa.yeisson.libreria_api.dto.UsuarioDTO;
import com.ochoa.yeisson.libreria_api.response.ApiResponse;
import com.ochoa.yeisson.libreria_api.service.UsuarioService;
import com.ochoa.yeisson.libreria_api.util.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioDTO>> crearUsuario(
            @Valid @RequestBody CrearUsuarioRequest request) {

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        usuarioService.crearUsuario(request),
                        "Usuario creado correctamente"
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> listarUsuarios() {

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        usuarioService.listarUsuarios(),
                        "Lista de usuarios"
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerPorId(@PathVariable Long id) {

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        usuarioService.obtenerPorId(id),
                        "Usuario encontrado"
                )
        );
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> buscar(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(ResponseBuilder.success(
                usuarioService.buscar(query),
                "Lista de usuarios"
        ));
    }
}
