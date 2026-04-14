package com.ochoa.yeisson.libreria_api.repository;

import com.ochoa.yeisson.libreria_api.model.Libro;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query("""
        SELECT l FROM Libro l
        WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(l.isbn) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Libro> buscarPorTituloOIsbn(@Param("query") String query, Pageable pageable);
}
