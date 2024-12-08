package com.payroll.PayrollSrv.controllers;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Integer id) {
        super("Usuario no encontrado con ID: " + id);
    }
}
