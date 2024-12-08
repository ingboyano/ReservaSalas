package com.payroll.PayrollSrv.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;

    @Column(name = "aula")
    private String aula; // Sala donde se realiza la reserva

    @Column(name = "fecha")
    private String fecha; // Fecha en formato "dd/MM/yyyy"

    @Column(name = "horario")
    private String horario; // Horario en formato "hh:mm"

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) // Foreign key to Usuario
    private Usuario usuario; // Link back to Usuario
}

