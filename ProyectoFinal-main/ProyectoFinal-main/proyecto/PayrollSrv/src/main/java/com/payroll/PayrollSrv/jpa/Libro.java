package com.payroll.PayrollSrv.jpa;

import jakarta.persistence.*;
import lombok.Data;
 
@Data
@Entity
@Table(name = "libros")
public class Libro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_libro")
    private Integer idLibro;
    private String titulo;
    private String autor;
    private Integer anho;
    private String genero;
    private String isbn;
    @Column(name = "portada_url")
    private String portadaUrl;
 
}