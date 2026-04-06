package com.ochoa.yeisson.libreria_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ochoa.yeisson.libreria_api.dto.CrearPrestamoRequest;
import com.ochoa.yeisson.libreria_api.dto.PrestamoDTO;
import com.ochoa.yeisson.libreria_api.enums.EstadoPrestamo;
import com.ochoa.yeisson.libreria_api.exception.GlobalExceptionHandler;
import com.ochoa.yeisson.libreria_api.exception.ResourceNotFoundException;
import com.ochoa.yeisson.libreria_api.service.PrestamoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrestamoController.class)
@Import(GlobalExceptionHandler.class)
public class PrestamoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrestamoService prestamoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void prestarLibro_ok() throws Exception {

        CrearPrestamoRequest request = new CrearPrestamoRequest();
        request.setLibroId(1L);
        request.setUsuarioId(1L);

        PrestamoDTO response = PrestamoDTO.builder()
                .id(1L)
                .estado(null)
                .build();

        when(prestamoService.prestarLibro(1L, 1L)).thenReturn(response);

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void prestarLibro_cuandoRequestInvalido_deberiaRetornar400() throws Exception {

        CrearPrestamoRequest request = new CrearPrestamoRequest();

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de validación"))
                .andExpect(jsonPath("$.errors.libroId").value("El libroId es obligatorio"))
                .andExpect(jsonPath("$.errors.usuarioId").value("El usuarioId es obligatorio"));
    }

    @Test
    void prestarLibro_cuandoFaltaUsuarioId_deberiaRetornar400() throws Exception {

        CrearPrestamoRequest request = new CrearPrestamoRequest();
        request.setLibroId(1L);

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.usuarioId")
                        .value("El usuarioId es obligatorio"));
    }

    @Test
    void listarPrestamos_deberiaRetornarLista() throws Exception {

        List<PrestamoDTO> prestamos = List.of(
                PrestamoDTO.builder().id(1L).libroId(1L).usuarioId(1L).build()
        );

        when(prestamoService.listarPrestamos()).thenReturn(prestamos);

        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].libroId").value(1L))
                .andExpect(jsonPath("$.data[0].usuarioId").value(1L));
    }

    @Test
    void devolverLibro_deberiaRetornarPrestamoDevuelto() throws Exception {

        Long prestamoId = 1L;

        PrestamoDTO response = PrestamoDTO.builder()
                .id(prestamoId)
                .estado(EstadoPrestamo.DEVUELTO)
                .build();

        when(prestamoService.devolverLibro(prestamoId)).thenReturn(response);

        mockMvc.perform(put("/api/prestamos/devolver/{id}", prestamoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Libro devuelto correctamente"))
                .andExpect(jsonPath("$.data.id").value(prestamoId))
                .andExpect(jsonPath("$.data.estado").value("DEVUELTO"));
    }

    @Test
    void devolverLibro_cuandoNoExiste_deberiaRetornar404() throws Exception {

        Long prestamoId = 99L;

        when(prestamoService.devolverLibro(prestamoId))
                .thenThrow(new ResourceNotFoundException("Préstamo no encontrado"));

        mockMvc.perform(put("/api/prestamos/devolver/{id}", prestamoId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Préstamo no encontrado"));
    }
}
