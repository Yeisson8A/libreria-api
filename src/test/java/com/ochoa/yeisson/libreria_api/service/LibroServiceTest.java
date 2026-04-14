package com.ochoa.yeisson.libreria_api.service;

import com.ochoa.yeisson.libreria_api.dto.CrearLibroRequest;
import com.ochoa.yeisson.libreria_api.dto.LibroDTO;
import com.ochoa.yeisson.libreria_api.exception.BadRequestException;
import com.ochoa.yeisson.libreria_api.exception.ResourceNotFoundException;
import com.ochoa.yeisson.libreria_api.mapper.LibroMapper;
import com.ochoa.yeisson.libreria_api.model.Libro;
import com.ochoa.yeisson.libreria_api.repository.LibroRepository;
import com.ochoa.yeisson.libreria_api.service.impl.LibroServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibroServiceTest {
    @Mock
    private LibroRepository libroRepository;

    @Mock
    private LibroMapper libroMapper;

    @InjectMocks
    private LibroServiceImpl libroService;

    @Test
    void crearLibro_ok() {

        CrearLibroRequest request = new CrearLibroRequest();
        request.setTitulo("Clean Code");
        request.setAutor("Robert");
        request.setIsbn("123");

        Libro libroGuardado = Libro.builder()
                .id(1L)
                .titulo("Clean Code")
                .build();

        LibroDTO dto = new LibroDTO();

        when(libroRepository.existsByIsbn("123")).thenReturn(false);
        when(libroRepository.save(any())).thenReturn(libroGuardado);
        when(libroMapper.toDTO(libroGuardado)).thenReturn(dto);

        LibroDTO result = libroService.crearLibro(request);

        assertNotNull(result);
        verify(libroRepository).save(any());
    }

    @Test
    void crearLibro_isbnDuplicado() {

        CrearLibroRequest request = new CrearLibroRequest();
        request.setIsbn("123");

        when(libroRepository.existsByIsbn("123")).thenReturn(true);

        assertThrows(BadRequestException.class, () ->
                libroService.crearLibro(request)
        );

        verify(libroRepository, never()).save(any());
    }

    @Test
    void listarLibros_ok() {

        List<Libro> libros = List.of(new Libro());
        List<LibroDTO> dtos = List.of(new LibroDTO());

        when(libroRepository.findAll()).thenReturn(libros);
        when(libroMapper.toDTOList(libros)).thenReturn(dtos);

        List<LibroDTO> result = libroService.listarLibros();

        assertEquals(1, result.size());
    }

    @Test
    void obtenerPorId_ok() {

        Libro libro = new Libro();
        LibroDTO dto = new LibroDTO();

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(libroMapper.toDTO(libro)).thenReturn(dto);

        LibroDTO result = libroService.obtenerPorId(1L);

        assertNotNull(result);
    }

    @Test
    void obtenerPorId_noExiste() {

        when(libroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                libroService.obtenerPorId(1L)
        );
    }

    @Test
    void actualizarLibro_ok() {

        CrearLibroRequest request = new CrearLibroRequest();
        request.setTitulo("Nuevo");

        Libro libro = new Libro();
        LibroDTO dto = new LibroDTO();

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(libroRepository.save(libro)).thenReturn(libro);
        when(libroMapper.toDTO(libro)).thenReturn(dto);

        LibroDTO result = libroService.actualizarLibro(1L, request);

        assertNotNull(result);
        assertEquals("Nuevo", libro.getTitulo());
    }

    @Test
    void actualizarLibro_noExiste() {

        when(libroRepository.findById(1L)).thenReturn(Optional.empty());

        CrearLibroRequest request = new CrearLibroRequest();

        assertThrows(ResourceNotFoundException.class, () ->
                libroService.actualizarLibro(1L, request)
        );
    }

    @Test
    void eliminarLibro_ok() {

        libroService.eliminarLibro(1L);

        verify(libroRepository).deleteById(1L);
    }

    @Test
    void deberiaUsarFindAllCuandoQueryEsNull() {

        List<Libro> libros = List.of(new Libro());
        Page<Libro> page = new PageImpl<>(libros);

        when(libroRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(libroMapper.toDTOList(libros)).thenReturn(List.of(new LibroDTO()));

        List<LibroDTO> result = libroService.buscar(null);

        assertNotNull(result);
        verify(libroRepository).findAll(PageRequest.of(0, 10));
        verify(libroMapper).toDTOList(libros);
    }

    @Test
    void deberiaUsarFindAllCuandoQueryEsVacia() {

        List<Libro> libros = List.of(new Libro());
        Page<Libro> page = new PageImpl<>(libros);

        when(libroRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(libroMapper.toDTOList(libros)).thenReturn(List.of(new LibroDTO()));

        List<LibroDTO> result = libroService.buscar("   ");

        assertNotNull(result);
        verify(libroRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    void deberiaBuscarPorTituloOIsbnCuandoQueryTieneValor() {

        List<Libro> libros = List.of(new Libro());

        when(libroRepository.buscarPorTituloOIsbn(eq("clean"), any(PageRequest.class)))
                .thenReturn(libros);

        when(libroMapper.toDTOList(libros))
                .thenReturn(List.of(new LibroDTO()));

        List<LibroDTO> result = libroService.buscar("clean");

        assertNotNull(result);

        verify(libroRepository).buscarPorTituloOIsbn(eq("clean"), any(PageRequest.class));
        verify(libroMapper).toDTOList(libros);
    }

    @Test
    void deberiaAplicarTrimALaQuery() {

        List<Libro> libros = List.of(new Libro());

        when(libroRepository.buscarPorTituloOIsbn(eq("clean"), any(PageRequest.class)))
                .thenReturn(libros);

        when(libroMapper.toDTOList(libros))
                .thenReturn(List.of(new LibroDTO()));

        libroService.buscar("  clean  ");

        verify(libroRepository).buscarPorTituloOIsbn(eq("clean"), any(PageRequest.class));
    }
}
