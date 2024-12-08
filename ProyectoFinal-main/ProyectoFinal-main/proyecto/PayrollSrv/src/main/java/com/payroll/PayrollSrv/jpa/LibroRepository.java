package com.payroll.PayrollSrv.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Integer> {
    // Método personalizado para buscar libro por ISBN
    Libro findByIsbn(String isbn);  // Asegúrate de que el tipo ISBN sea String en el modelo
}