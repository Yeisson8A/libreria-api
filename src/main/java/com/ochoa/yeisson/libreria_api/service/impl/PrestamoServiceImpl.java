package com.ochoa.yeisson.libreria_api.service.impl;

import com.ochoa.yeisson.libreria_api.dto.PrestamoDTO;
import com.ochoa.yeisson.libreria_api.enums.EstadoPrestamo;
import com.ochoa.yeisson.libreria_api.exception.BusinessException;
import com.ochoa.yeisson.libreria_api.exception.ResourceNotFoundException;
import com.ochoa.yeisson.libreria_api.mapper.PrestamoMapper;
import com.ochoa.yeisson.libreria_api.model.*;
import com.ochoa.yeisson.libreria_api.repository.*;
import com.ochoa.yeisson.libreria_api.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements PrestamoService {
    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoMapper prestamoMapper;

    @Override
    @Transactional
    public PrestamoDTO prestarLibro(Long libroId, Long usuarioId) {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        if (!libro.getDisponible()) {
            throw new BusinessException("El libro no está disponible");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Prestamo prestamo = Prestamo.builder()
                .libro(libro)
                .usuario(usuario)
                .fechaPrestamo(LocalDate.now())
                .estado(EstadoPrestamo.PRESTADO)
                .build();

        libro.setDisponible(false);
        libroRepository.save(libro);

        Prestamo saved = prestamoRepository.save(prestamo);

        return prestamoMapper.toDTO(saved);
    }

    @Override
    public PrestamoDTO devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado"));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BusinessException("El libro ya fue devuelto");
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        Libro libro = prestamo.getLibro();
        libro.setDisponible(true);
        libroRepository.save(libro);

        Prestamo updated = prestamoRepository.save(prestamo);

        return prestamoMapper.toDTO(updated);
    }

    @Override
    public List<PrestamoDTO> listarPrestamos() {
        return prestamoRepository.findAll()
                .stream()
                .map(prestamoMapper::toDTO)
                .toList();
    }
}
