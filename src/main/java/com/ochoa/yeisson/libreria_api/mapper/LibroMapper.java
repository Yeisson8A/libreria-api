package com.ochoa.yeisson.libreria_api.mapper;

import com.ochoa.yeisson.libreria_api.dto.LibroDTO;
import com.ochoa.yeisson.libreria_api.model.Libro;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LibroMapper {
    LibroDTO toDTO(Libro libro);

    Libro toEntity(LibroDTO dto);

    List<LibroDTO> toDTOList(List<Libro> libros);
}
