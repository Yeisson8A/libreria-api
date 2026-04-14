package com.ochoa.yeisson.libreria_api.service.impl;

import com.ochoa.yeisson.libreria_api.dto.CrearUsuarioRequest;
import com.ochoa.yeisson.libreria_api.dto.UsuarioDTO;
import com.ochoa.yeisson.libreria_api.exception.BusinessException;
import com.ochoa.yeisson.libreria_api.exception.ResourceNotFoundException;
import com.ochoa.yeisson.libreria_api.mapper.UsuarioMapper;
import com.ochoa.yeisson.libreria_api.model.Usuario;
import com.ochoa.yeisson.libreria_api.repository.UsuarioRepository;
import com.ochoa.yeisson.libreria_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UsuarioDTO crearUsuario(CrearUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El email ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .build();

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Override
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioMapper.toDTOList(usuarioRepository.findAll());
    }

    @Override
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return usuarioMapper.toDTO(usuario);
    }

    @Override
    public List<UsuarioDTO> buscar(String query) {
        if (query == null || query.trim().isEmpty()) {
            return usuarioMapper.toDTOList(usuarioRepository.findAll(PageRequest.of(0, 10)).getContent());
        }

        return usuarioMapper.toDTOList(usuarioRepository.buscarPorNombre(query.trim(), PageRequest.of(0, 10)));
    }
}
