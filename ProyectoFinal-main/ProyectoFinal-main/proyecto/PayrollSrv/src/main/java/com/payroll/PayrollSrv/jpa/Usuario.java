package com.payroll.PayrollSrv.jpa;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    private String contrasenha;

    // Relación con las reservas
    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Reserva> reservas;
}
