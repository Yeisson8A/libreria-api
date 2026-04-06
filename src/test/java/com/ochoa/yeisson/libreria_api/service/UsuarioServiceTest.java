package com.ochoa.yeisson.libreria_api.service;

import com.ochoa.yeisson.libreria_api.dto.CrearUsuarioRequest;
import com.ochoa.yeisson.libreria_api.dto.UsuarioDTO;
import com.ochoa.yeisson.libreria_api.exception.BusinessException;
import com.ochoa.yeisson.libreria_api.exception.ResourceNotFoundException;
import com.ochoa.yeisson.libreria_api.mapper.UsuarioMapper;
import com.ochoa.yeisson.libreria_api.model.Usuario;
import com.ochoa.yeisson.libreria_api.repository.UsuarioRepository;
import com.ochoa.yeisson.libreria_api.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void crearUsuario_ok() {

        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setNombre("Juan");
        request.setEmail("juan@test.com");

        Usuario usuarioGuardado = Usuario.builder()
                .id(1L)
                .nombre("Juan")
                .email("juan@test.com")
                .build();

        UsuarioDTO dto = new UsuarioDTO();

        when(usuarioRepository.existsByEmail("juan@test.com")).thenReturn(false);
        when(usuarioRepository.save(any())).thenReturn(usuarioGuardado);
        when(usuarioMapper.toDTO(usuarioGuardado)).thenReturn(dto);

        UsuarioDTO result = usuarioService.crearUsuario(request);

        assertNotNull(result);
        verify(usuarioRepository).save(any());
    }

    @Test
    void crearUsuario_emailDuplicado() {

        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setEmail("test@test.com");

        when(usuarioRepository.existsByEmail("test@test.com")).thenReturn(true);

        assertThrows(BusinessException.class, () ->
                usuarioService.crearUsuario(request)
        );

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void listarUsuarios_ok() {

        List<Usuario> usuarios = List.of(new Usuario());
        List<UsuarioDTO> dtos = List.of(new UsuarioDTO());

        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(usuarioMapper.toDTOList(usuarios)).thenReturn(dtos);

        List<UsuarioDTO> result = usuarioService.listarUsuarios();

        assertEquals(1, result.size());
    }

    @Test
    void obtenerPorId_ok() {

        Usuario usuario = new Usuario();
        UsuarioDTO dto = new UsuarioDTO();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(usuario)).thenReturn(dto);

        UsuarioDTO result = usuarioService.obtenerPorId(1L);

        assertNotNull(result);
    }

    @Test
    void obtenerPorId_noExiste() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                usuarioService.obtenerPorId(1L)
        );
    }
}
