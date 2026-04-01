package com.ochoa.yeisson.libreria_api.mapper;

import com.ochoa.yeisson.libreria_api.dto.UsuarioDTO;
import com.ochoa.yeisson.libreria_api.model.Usuario;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTO dto);

    List<UsuarioDTO> toDTOList(List<Usuario> usuarios);
}
