package com.payroll.PayrollSrv.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.payroll.PayrollSrv.jpa.Reserva;
import com.payroll.PayrollSrv.jpa.ReservaRepository;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaRepository reservaRepository;

    public ReservaController(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    // Obtener una reserva por su ID
    @GetMapping("/{id}")
    public Reserva getReservaById(@PathVariable Integer id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    // Obtener todas las reservas
    @GetMapping
    public List<Reserva> getAllReservas() {
        return reservaRepository.findAll();
    }

    // Crear una nueva reserva
    @PostMapping
    public Reserva createReserva(@RequestBody Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    // Actualizar una reserva
    @PutMapping("/{id}")
    public Reserva updateReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        return reservaRepository.findById(id)
                .map(existingReserva -> {
                    existingReserva.setAula(reserva.getAula());
                    existingReserva.setFecha(reserva.getFecha());
                    existingReserva.setHorario(reserva.getHorario());
                    return reservaRepository.save(existingReserva);
                })
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    // Eliminar una reserva
    @DeleteMapping("/{id}")
    public Reserva deleteReserva(@PathVariable Integer id) {
        return reservaRepository.findById(id)
                .map(reserva -> {
                    reservaRepository.delete(reserva);
                    return reserva;
                })
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }
}
