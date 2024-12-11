package com.payroll.PayrollSrv.jpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    Reserva findByAulaAndHorario(String aula, String horario);
}
