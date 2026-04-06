package com.ochoa.yeisson.libreria_api.service;

import com.ochoa.yeisson.libreria_api.dto.LibroDTO;
import com.ochoa.yeisson.libreria_api.dto.PrestamoDTO;
import com.ochoa.yeisson.libreria_api.enums.EstadoPrestamo;
import com.ochoa.yeisson.libreria_api.exception.BusinessException;
import com.ochoa.yeisson.libreria_api.exception.ResourceNotFoundException;
import com.ochoa.yeisson.libreria_api.mapper.PrestamoMapper;
import com.ochoa.yeisson.libreria_api.model.Libro;
import com.ochoa.yeisson.libreria_api.model.Prestamo;
import com.ochoa.yeisson.libreria_api.model.Usuario;
import com.ochoa.yeisson.libreria_api.repository.LibroRepository;
import com.ochoa.yeisson.libreria_api.repository.PrestamoRepository;
import com.ochoa.yeisson.libreria_api.repository.UsuarioRepository;
import com.ochoa.yeisson.libreria_api.service.impl.PrestamoServiceImpl;
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
public class PrestamoServiceTest {
    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PrestamoMapper prestamoMapper;

    @InjectMocks
    private PrestamoServiceImpl prestamoService;

    @Test
    void prestarLibro_ok() {

        Long libroId = 1L;
        Long usuarioId = 1L;

        Libro libro = new Libro();
        libro.setId(libroId);
        libro.setDisponible(true);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Prestamo prestamo = new Prestamo();
        PrestamoDTO dto = new PrestamoDTO();

        when(libroRepository.findById(libroId)).thenReturn(Optional.of(libro));
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(prestamoRepository.save(any())).thenReturn(prestamo);
        when(prestamoMapper.toDTO(prestamo)).thenReturn(dto);

        PrestamoDTO result = prestamoService.prestarLibro(libroId, usuarioId);

        assertNotNull(result);
        verify(prestamoRepository).save(any());
        verify(libroRepository).save(libro);
    }

    @Test
    void prestarLibro_libroNoExiste() {

        when(libroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                prestamoService.prestarLibro(1L, 1L)
        );
    }

    @Test
    void prestarLibro_usuarioNoExiste() {

        Libro libro = new Libro();
        libro.setDisponible(true);

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                prestamoService.prestarLibro(1L, 1L)
        );
    }

    @Test
    void prestarLibro_libroNoDisponible() {

        Libro libro = new Libro();
        libro.setDisponible(false);

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        assertThrows(BusinessException.class, () ->
                prestamoService.prestarLibro(1L, 1L)
        );
    }

    @Test
    void devolverLibro_ok() {

        Prestamo prestamo = new Prestamo();
        prestamo.setId(1L);

        Libro libro = new Libro();
        prestamo.setLibro(libro);

        PrestamoDTO dto = new PrestamoDTO();

        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));
        when(prestamoRepository.save(any())).thenReturn(prestamo);
        when(prestamoMapper.toDTO(prestamo)).thenReturn(dto);

        PrestamoDTO result = prestamoService.devolverLibro(1L);

        assertNotNull(result);
        verify(prestamoRepository).save(prestamo);
        verify(libroRepository).save(libro);
    }

    @Test
    void devolverLibro_yaDevuelto() {

        Libro libro = new Libro();
        Prestamo prestamo = new Prestamo();
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamo.setLibro(libro);

        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        assertThrows(BusinessException.class, () ->
                prestamoService.devolverLibro(1L)
        );
    }

    @Test
    void devolverLibro_prestamoNoExiste() {

        Long prestamoId = 1L;

        // Simulamos que NO existe en DB
        when(prestamoRepository.findById(prestamoId))
                .thenReturn(Optional.empty());

        // Verificamos que lanza excepción
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> prestamoService.devolverLibro(prestamoId)
        );

        // Validamos el mensaje (opcional pero PRO)
        assertEquals("Préstamo no encontrado", exception.getMessage());

        // Verificamos interacción
        verify(prestamoRepository).findById(prestamoId);

        // Aseguramos que NO sigue ejecutando lógica
        verify(libroRepository, never()).save(any());
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void listarPrestamos_ok() {

        // Arrange
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setId(1L);

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setId(2L);

        List<Prestamo> prestamos = List.of(prestamo1, prestamo2);

        PrestamoDTO dto1 = new PrestamoDTO();
        dto1.setId(1L);

        PrestamoDTO dto2 = new PrestamoDTO();
        dto2.setId(2L);

        when(prestamoRepository.findAll()).thenReturn(prestamos);
        when(prestamoMapper.toDTO(prestamo1)).thenReturn(dto1);
        when(prestamoMapper.toDTO(prestamo2)).thenReturn(dto2);

        // Act
        List<PrestamoDTO> result = prestamoService.listarPrestamos();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(prestamoRepository).findAll();
        verify(prestamoMapper).toDTO(prestamo1);
        verify(prestamoMapper).toDTO(prestamo2);
    }

    @Test
    void listarPrestamos_listaVacia() {

        when(prestamoRepository.findAll()).thenReturn(List.of());

        List<PrestamoDTO> result = prestamoService.listarPrestamos();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(prestamoRepository).findAll();
    }
}
