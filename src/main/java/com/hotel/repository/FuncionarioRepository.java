package com.hotel.repository;

import com.hotel.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {
    List<Funcionario> findByHotelId(String hotelId);
    Optional<Funcionario> findByCpf(String cpf);
    List<Funcionario> findByNomeContainingIgnoreCase(String nome);
}
