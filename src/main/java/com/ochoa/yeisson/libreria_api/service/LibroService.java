package com.ochoa.yeisson.libreria_api.service;

import com.ochoa.yeisson.libreria_api.dto.CrearLibroRequest;
import com.ochoa.yeisson.libreria_api.dto.LibroDTO;
import java.util.List;

public interface LibroService {
    LibroDTO crearLibro(CrearLibroRequest request);

    List<LibroDTO> listarLibros();

    LibroDTO obtenerPorId(Long id);

    LibroDTO actualizarLibro(Long id, CrearLibroRequest request);

    void eliminarLibro(Long id);
}
