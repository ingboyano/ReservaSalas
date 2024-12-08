package com.payroll.PayrollSrv.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Método para encontrar un usuario por su nombre de usuario
    Usuario findByNombreUsuario(String nombreUsuario);
}
