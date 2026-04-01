package com.ochoa.yeisson.libreria_api.repository;

import com.ochoa.yeisson.libreria_api.enums.EstadoPrestamo;
import com.ochoa.yeisson.libreria_api.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByUsuarioId(Long usuarioId);

    List<Prestamo> findByLibroId(Long libroId);

    List<Prestamo> findByEstado(EstadoPrestamo estado);

    List<Prestamo> findByUsuarioIdAndEstado(Long usuarioId, EstadoPrestamo estado);
}
