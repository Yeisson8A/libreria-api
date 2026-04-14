package com.ochoa.yeisson.libreria_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ochoa.yeisson.libreria_api.dto.CrearUsuarioRequest;
import com.ochoa.yeisson.libreria_api.dto.UsuarioDTO;
import com.ochoa.yeisson.libreria_api.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearUsuario_ok() throws Exception {

        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setNombre("Juan");
        request.setEmail("juan@test.com");

        UsuarioDTO response = UsuarioDTO.builder()
                .id(1L)
                .nombre("Juan")
                .email("juan@test.com")
                .build();

        when(usuarioService.crearUsuario(request)).thenReturn(response);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre").value("Juan"));
    }

    @Test
    void listarUsuarios_deberiaRetornarLista() throws Exception {

        List<UsuarioDTO> usuarios = List.of(
                UsuarioDTO.builder().id(1L).nombre("Juan").build()
        );

        when(usuarioService.listarUsuarios()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].nombre").value("Juan"));
    }

    @Test
    void obtenerPorId_deberiaRetornarUsuario() throws Exception {

        UsuarioDTO usuario = UsuarioDTO.builder()
                .id(1L)
                .nombre("Juan")
                .build();

        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void deberiaRetornarUsuariosConQuery() throws Exception {

        List<UsuarioDTO> mockUsuarios = List.of(
                UsuarioDTO.builder().id(1L).nombre("Juan").build(),
                UsuarioDTO.builder().id(2L).nombre("Ana").build()
        );

        when(usuarioService.buscar("juan")).thenReturn(mockUsuarios);

        mockMvc.perform(get("/api/usuarios/buscar")
                        .param("query", "juan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lista de usuarios"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].nombre").value("Juan"));
    }

    @Test
    void deberiaRetornarUsuariosSinQuery() throws Exception {

        List<UsuarioDTO> mockUsuarios = List.of(
                UsuarioDTO.builder().id(1L).nombre("Carlos").build()
        );

        when(usuarioService.buscar(null)).thenReturn(mockUsuarios);

        mockMvc.perform(get("/api/usuarios/buscar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void deberiaRetornarListaVacia() throws Exception {

        when(usuarioService.buscar("xyz")).thenReturn(List.of());

        mockMvc.perform(get("/api/usuarios/buscar")
                        .param("query", "xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    void deberiaLlamarAlServiceConQuery() throws Exception {

        when(usuarioService.buscar("test")).thenReturn(List.of());

        mockMvc.perform(get("/api/usuarios/buscar")
                .param("query", "test"));

        verify(usuarioService).buscar("test");
    }
}
