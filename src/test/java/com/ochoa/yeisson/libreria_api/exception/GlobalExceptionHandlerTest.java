package com.ochoa.yeisson.libreria_api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ochoa.yeisson.libreria_api.exception.support.TestExceptionController;
import com.ochoa.yeisson.libreria_api.exception.support.TestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestExceptionController.class)
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void handleNotFound() throws Exception {

        mockMvc.perform(get("/test-exceptions/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("No encontrado"));
    }

    @Test
    void handleBadRequest() throws Exception {

        mockMvc.perform(get("/test-exceptions/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Petición inválida"));
    }

    @Test
    void handleBusiness() throws Exception {

        mockMvc.perform(get("/test-exceptions/business"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de negocio"));
    }

    @Test
    void handleGeneral() throws Exception {

        mockMvc.perform(get("/test-exceptions/general"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error interno del servidor"));
    }

    @Test
    void handleValidationErrors() throws Exception {

        TestRequest request = new TestRequest();

        mockMvc.perform(post("/test-exceptions/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de validación"))
                .andExpect(jsonPath("$.errors.campo").value("campo es obligatorio"));
    }
}
