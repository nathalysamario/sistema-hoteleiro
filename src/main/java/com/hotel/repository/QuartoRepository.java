package com.hotel.repository;

import com.hotel.model.Quarto;
import com.hotel.model.SituacaoQuarto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuartoRepository extends JpaRepository<Quarto, String> {
    List<Quarto> findByHotelId(String hotelId);
    List<Quarto> findBySituacao(SituacaoQuarto situacao);
    List<Quarto> findByHotelIdAndSituacao(String hotelId, SituacaoQuarto situacao);
    boolean existsByNumeroAndHotelId(Integer numero, String hotelId);
}
