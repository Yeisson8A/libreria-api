package com.ochoa.yeisson.libreria_api.dto;

import com.ochoa.yeisson.libreria_api.enums.EstadoPrestamo;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestamoDTO {
    private Long id;

    private Long libroId;
    private String tituloLibro;

    private Long usuarioId;
    private String nombreUsuario;

    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    private EstadoPrestamo estado;
}
