package com.ochoa.yeisson.libreria_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ochoa.yeisson.libreria_api.dto.CrearLibroRequest;
import com.ochoa.yeisson.libreria_api.dto.LibroDTO;
import com.ochoa.yeisson.libreria_api.exception.ResourceNotFoundException;
import com.ochoa.yeisson.libreria_api.service.LibroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibroController.class)
public class LibroControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibroService libroService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearLibro_deberiaRetornarLibro() throws Exception {

        CrearLibroRequest request = new CrearLibroRequest();
        request.setTitulo("Clean Code");
        request.setAutor("Robert C. Martin");
        request.setIsbn("1234567890");
        request.setFechaPublicacion(LocalDate.now());

        LibroDTO response = LibroDTO.builder()
                .id(1L)
                .titulo("Clean Code")
                .autor("Robert C. Martin")
                .isbn("1234567890")
                .disponible(true)
                .build();

        when(libroService.crearLibro(request)).thenReturn(response);

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.titulo").value("Clean Code"));
    }

    @Test
    void listarLibros_deberiaRetornarLista() throws Exception {

        List<LibroDTO> libros = List.of(
                LibroDTO.builder().id(1L).titulo("Clean Code").build()
        );

        when(libroService.listarLibros()).thenReturn(libros);

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].titulo").value("Clean Code"));
    }

    @Test
    void obtenerPorId_deberiaRetornarLibro() throws Exception {

        LibroDTO libro = LibroDTO.builder()
                .id(1L)
                .titulo("Clean Code")
                .build();

        when(libroService.obtenerPorId(1L)).thenReturn(libro);

        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void crearLibro_deberiaFallarPorValidacion() throws Exception {

        CrearLibroRequest request = new CrearLibroRequest(); // vacío

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void actualizarLibro_ok() throws Exception {

        Long id = 1L;

        CrearLibroRequest request = new CrearLibroRequest();
        request.setTitulo("Clean Code");
        request.setAutor("Robert C. Martin");
        request.setIsbn("1234567890");
        request.setFechaPublicacion(LocalDate.now());

        LibroDTO response = new LibroDTO();
        response.setId(id);
        response.setTitulo("Clean Code");
        response.setAutor("Robert C. Martin");
        response.setIsbn("1234567890");
        response.setFechaPublicacion(LocalDate.now());

        when(libroService.actualizarLibro(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/api/libros/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Libro actualizado correctamente"))
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.titulo").value("Clean Code"));

        verify(libroService).actualizarLibro(eq(id), any());
    }

    @Test
    void actualizarLibro_noExiste() throws Exception {

        Long id = 1L;

        CrearLibroRequest request = new CrearLibroRequest();
        request.setTitulo("Clean Code");
        request.setAutor("Robert C. Martin");
        request.setIsbn("1234567890");
        request.setFechaPublicacion(LocalDate.now());

        when(libroService.actualizarLibro(eq(id), any()))
                .thenThrow(new ResourceNotFoundException("Libro no encontrado"));

        mockMvc.perform(put("/api/libros/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Libro no encontrado"));
    }

    @Test
    void actualizarLibro_requestInvalido() throws Exception {

        CrearLibroRequest request = new CrearLibroRequest();

        mockMvc.perform(put("/api/libros/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void eliminarLibro_ok() throws Exception {

        Long id = 1L;

        doNothing().when(libroService).eliminarLibro(id);

        mockMvc.perform(delete("/api/libros/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Libro eliminado correctamente"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(libroService).eliminarLibro(id);
    }

    @Test
    void eliminarLibro_noExiste() throws Exception {

        Long id = 1L;

        doThrow(new ResourceNotFoundException("Libro no encontrado"))
                .when(libroService).eliminarLibro(id);

        mockMvc.perform(delete("/api/libros/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Libro no encontrado"));
    }
}
