package com.hotel.repository;

import com.hotel.model.Reserva;
import com.hotel.model.SituacaoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, String> {
    List<Reserva> findByHospedeId(String hospedeId);
    List<Reserva> findByQuartoId(String quartoId);
    List<Reserva> findBySituacao(SituacaoReserva situacao);

    @Query("SELECT r FROM Reserva r WHERE r.quarto.id = :quartoId " +
           "AND r.situacao NOT IN ('CANCELADA', 'FINALIZADA') " +
           "AND (r.dataEntrada < :dataSaida AND r.dataSaida > :dataEntrada)")
    List<Reserva> findConflitos(@Param("quartoId") String quartoId,
                                @Param("dataEntrada") LocalDate dataEntrada,
                                @Param("dataSaida") LocalDate dataSaida);
}
