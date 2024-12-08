package com.payroll.PayrollSrv.controllers;

import com.payroll.PayrollSrv.jpa.Usuario;
import com.payroll.PayrollSrv.jpa.AuthService;
import com.payroll.PayrollSrv.jpa.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Autenticar al usuario con el nombre de usuario y contraseña
        Usuario usuario = authService.authenticate(loginRequest.getNombreUsuario(), loginRequest.getContrasenha());

        if (usuario != null) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
        }
    }
}