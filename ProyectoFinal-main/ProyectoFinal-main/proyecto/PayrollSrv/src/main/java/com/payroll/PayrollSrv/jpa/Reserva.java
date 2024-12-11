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
    private String aula;

    @Column(name = "fecha")
    private String fecha;

    @Column(name = "horario")
    private String horario;

    // Relación con el usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}