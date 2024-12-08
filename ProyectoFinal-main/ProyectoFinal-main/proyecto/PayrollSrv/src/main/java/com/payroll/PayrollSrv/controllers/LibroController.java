package com.payroll.PayrollSrv.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.payroll.PayrollSrv.jpa.Libro;
import com.payroll.PayrollSrv.jpa.LibroRepository;

import jakarta.persistence.EntityManager;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    // Endpoint para listar libros
    @GetMapping
    public List<Libro> getAllLibros() {
        return libroRepository.findAll();
    }

    // Endpoint para insertar un nuevo libro
    @PostMapping
    public ResponseEntity<Libro> createLibro(@RequestBody Libro libro) {
        try {
            Libro nuevoLibro = libroRepository.save(libro);
            return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // (Opcional) Endpoint para eliminar libro
    @Autowired
private EntityManager entityManager;  // Inyectar EntityManager

@DeleteMapping("/{isbn}")
public ResponseEntity<HttpStatus> deleteLibro(@PathVariable("isbn") String isbn) {
    try {
        Libro libro = libroRepository.findByIsbn(isbn);
        if (libro != null) {
            libroRepository.delete(libro);
            libroRepository.flush(); 
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
}
