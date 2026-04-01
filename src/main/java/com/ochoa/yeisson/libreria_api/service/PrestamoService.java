package com.ochoa.yeisson.libreria_api.service;

import com.ochoa.yeisson.libreria_api.dto.PrestamoDTO;
import java.util.List;

public interface PrestamoService {
    PrestamoDTO prestarLibro(Long libroId, Long usuarioId);

    PrestamoDTO devolverLibro(Long prestamoId);

    List<PrestamoDTO> listarPrestamos();
}
