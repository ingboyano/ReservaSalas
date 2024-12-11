package com.payroll.PayrollSrv.controllers;

import com.payroll.PayrollSrv.jpa.Reserva;
import com.payroll.PayrollSrv.jpa.ReservaRepository;
import com.payroll.PayrollSrv.jpa.Usuario;
import com.payroll.PayrollSrv.jpa.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/reserva/{id}")
    public Reserva obtenerReserva(@PathVariable Integer id) {
        Reserva reserva = reservaRepository.findById(id).orElse(null);
        if (reserva != null) {
            // Aquí puedes acceder al usuario y sus reservas.
            System.out.println(reserva.getUsuario().getNombreUsuario());
        }
        return reserva;
    }

    // Eliminar una reserva por aula y horario
    @DeleteMapping("/aula/{aula}/horario/{horario}")
    public String deleteReservaByAulaAndHorario(@PathVariable String aula, @PathVariable String horario) {
        // Buscar la reserva que coincida con aula y horario
        Reserva reserva = reservaRepository.findByAulaAndHorario(aula, horario);

        if (reserva != null) {
            reservaRepository.delete(reserva);
            return "Reserva eliminada con éxito: Aula: " + aula + ", Horario: " + horario;
        } else {
            throw new RuntimeException("Reserva no encontrada con Aula: " + aula + " y Horario: " + horario);
        }
    }


}