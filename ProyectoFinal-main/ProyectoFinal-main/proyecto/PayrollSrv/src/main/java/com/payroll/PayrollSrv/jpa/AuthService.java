package com.payroll.PayrollSrv.jpa;

import com.payroll.PayrollSrv.jpa.Usuario;
import com.payroll.PayrollSrv.jpa.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario authenticate(String nombreUsuario, String contrasenha) {
        // Busca el usuario por su nombre de usuario
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);

        // Verifica si el usuario existe y si las contraseñas coinciden
        if (usuario != null && usuario.getContrasenha().equals(contrasenha)) {
            return usuario; // Usuario autenticado correctamente
        }
        return null; // Si no coincide, devuelve null
    }
}
