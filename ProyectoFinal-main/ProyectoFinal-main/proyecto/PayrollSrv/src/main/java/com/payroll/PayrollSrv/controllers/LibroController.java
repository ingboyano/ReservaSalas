package com.payroll.PayrollSrv.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.payroll.PayrollSrv.jpa.Libro;
import com.payroll.PayrollSrv.jpa.LibroRepository;

@RestController
public class LibroController {


    private final LibroRepository repository;

    public LibroController(LibroRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/libros/{id}")
    public Libro getLibroById(@PathVariable Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new LibroNotFoundException(id));
    }

    @GetMapping("/libros")
    public List<Libro> getAllLibros(){
        return repository.findAll();

    }


    @PostMapping("/libros")
    public Libro createLibro( @RequestBody Libro libro) {
        return repository.save(libro);
    }

    @PutMapping("/libros/{id}")
    public Libro updateLibro(@PathVariable Integer id, @RequestBody Libro libro) {
        return repository.findById(id)
            .map(libro1 -> {
                libro1.setTitulo(libro.getTitulo());
                libro1.setAutor(libro.getAutor());
                libro1.setIsbn(libro.getIsbn());
                return repository.save(libro1);
            })
            .orElseThrow(() -> new LibroNotFoundException(id));
    }

    @DeleteMapping("/libros/{id}")
    public Libro deleteLibro(@PathVariable Integer id) {
        return repository.findById(id)
            .map(libro -> {
                repository.delete(libro);
                return libro;
            })
            .orElseThrow(() -> new LibroNotFoundException(id));
    }

    
}
