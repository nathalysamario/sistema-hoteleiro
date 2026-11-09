package com.hotel.repository;

import com.hotel.model.Hospede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HospedeRepository extends JpaRepository<Hospede, String> {
    Optional<Hospede> findByCpf(String cpf);
    List<Hospede> findByNomeContainingIgnoreCase(String nome);
}
