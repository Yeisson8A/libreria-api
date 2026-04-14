package com.ochoa.yeisson.libreria_api.repository;

import com.ochoa.yeisson.libreria_api.model.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
        SELECT u FROM Usuario u
        WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Usuario> buscarPorNombre(@Param("query") String query, Pageable pageable);
}
