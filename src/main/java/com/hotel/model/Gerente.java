package com.hotel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "gerentes")
public class Gerente extends Funcionario {

    public Gerente() {}

    public Gerente(String nome, String cpf, Double salario, LocalDate dataContratacao, Hotel hotel) {
        super(nome, cpf, "Gerente", salario, dataContratacao, hotel);
    }

    @Override
    public String getTipoCargo() {
        return "Gerente";
    }
}
