package com.ochoa.yeisson.libreria_api.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibroDTO {
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private LocalDate fechaPublicacion;
    private Boolean disponible;
}
