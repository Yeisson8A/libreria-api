package com.ochoa.yeisson.libreria_api.service.impl;

import com.ochoa.yeisson.libreria_api.dto.CrearLibroRequest;
import com.ochoa.yeisson.libreria_api.dto.LibroDTO;
import com.ochoa.yeisson.libreria_api.exception.BadRequestException;
import com.ochoa.yeisson.libreria_api.exception.ResourceNotFoundException;
import com.ochoa.yeisson.libreria_api.mapper.LibroMapper;
import com.ochoa.yeisson.libreria_api.model.Libro;
import com.ochoa.yeisson.libreria_api.repository.LibroRepository;
import com.ochoa.yeisson.libreria_api.service.LibroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService {
    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;

    @Override
    public LibroDTO crearLibro(CrearLibroRequest request) {
        if (libroRepository.existsByIsbn(request.getIsbn())) {
            throw new BadRequestException("El ISBN ya existe");
        }

        Libro libro = Libro.builder()
                .titulo(request.getTitulo())
                .autor(request.getAutor())
                .isbn(request.getIsbn())
                .fechaPublicacion(request.getFechaPublicacion())
                .disponible(true)
                .build();

        return libroMapper.toDTO(libroRepository.save(libro));
    }

    @Override
    public List<LibroDTO> listarLibros() {
        return libroMapper.toDTOList(libroRepository.findAll());
    }

    @Override
    public LibroDTO obtenerPorId(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        return libroMapper.toDTO(libro);
    }

    @Override
    public LibroDTO actualizarLibro(Long id, CrearLibroRequest request) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setIsbn(request.getIsbn());
        libro.setFechaPublicacion(request.getFechaPublicacion());

        return libroMapper.toDTO(libroRepository.save(libro));
    }

    @Override
    public void eliminarLibro(Long id) {
        libroRepository.deleteById(id);
    }

    @Override
    public List<LibroDTO> buscar(String query) {
        if (query == null || query.trim().isEmpty()) {
            return libroMapper.toDTOList(libroRepository.findAll(PageRequest.of(0, 10)).getContent());
        }

        return libroMapper.toDTOList(libroRepository.buscarPorTituloOIsbn(query.trim(), PageRequest.of(0, 10)));
    }
}
