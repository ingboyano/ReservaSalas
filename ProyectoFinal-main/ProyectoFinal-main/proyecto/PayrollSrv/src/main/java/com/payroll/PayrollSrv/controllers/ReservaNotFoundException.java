package com.payroll.PayrollSrv.controllers;

public class ReservaNotFoundException extends RuntimeException {

    public ReservaNotFoundException(Integer id) {
        super("Reserva inexistente con ID: " + id);
    }
}
