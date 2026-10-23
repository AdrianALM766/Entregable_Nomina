package com.nomina.portalnomina.repository;

import com.nomina.portalnomina.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> { // <-- Integer aquí también
    Optional<Usuario> findByCedulaAndActivoTrue(String cedula);
}