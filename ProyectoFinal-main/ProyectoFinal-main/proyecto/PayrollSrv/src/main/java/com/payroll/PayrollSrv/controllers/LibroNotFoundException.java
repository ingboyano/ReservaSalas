package com.payroll.PayrollSrv.controllers;

class LibroNotFoundException extends RuntimeException {

    LibroNotFoundException(Integer id) {
      super("Libro inexistente " + id);
    }

  }