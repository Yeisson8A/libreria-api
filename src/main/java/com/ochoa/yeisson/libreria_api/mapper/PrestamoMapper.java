package com.ochoa.yeisson.libreria_api.mapper;

import com.ochoa.yeisson.libreria_api.dto.PrestamoDTO;
import com.ochoa.yeisson.libreria_api.model.Prestamo;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {
    @Mapping(source = "libro.id", target = "libroId")
    @Mapping(source = "libro.titulo", target = "tituloLibro")
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    PrestamoDTO toDTO(Prestamo prestamo);
}
