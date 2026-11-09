package com.hotel.repository;

import com.hotel.model.Servico;
import com.hotel.model.TipoServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, String> {
    List<Servico> findByTipo(TipoServico tipo);
    List<Servico> findByNomeContainingIgnoreCase(String nome);
}
