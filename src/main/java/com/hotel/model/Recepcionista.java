package com.hotel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "recepcionistas")
public class Recepcionista extends Funcionario {

    public Recepcionista() {}

    public Recepcionista(String nome, String cpf, Double salario, LocalDate dataContratacao, Hotel hotel) {
        super(nome, cpf, "Recepcionista", salario, dataContratacao, hotel);
    }

    @Override
    public String getTipoCargo() {
        return "Recepcionista";
    }
}
