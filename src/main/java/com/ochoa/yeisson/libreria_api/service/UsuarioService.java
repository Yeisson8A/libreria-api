package com.ochoa.yeisson.libreria_api.service;

import com.ochoa.yeisson.libreria_api.dto.CrearUsuarioRequest;
import com.ochoa.yeisson.libreria_api.dto.UsuarioDTO;
import java.util.List;

public interface UsuarioService {
    UsuarioDTO crearUsuario(CrearUsuarioRequest request);

    List<UsuarioDTO> listarUsuarios();

    UsuarioDTO obtenerPorId(Long id);
}
