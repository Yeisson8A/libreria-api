package com.ochoa.yeisson.libreria_api.repository;

import com.ochoa.yeisson.libreria_api.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}
