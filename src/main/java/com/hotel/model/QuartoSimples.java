package com.hotel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "quartos_simples")
public class QuartoSimples extends Quarto {

    public QuartoSimples() {}

    public QuartoSimples(Integer numero, Integer capacidade, Double valorDiaria, Hotel hotel) {
        super(numero, capacidade, valorDiaria, hotel);
    }

    @Override
    public double calcularValorTotal(int dias) {
        return getValorDiaria() * dias;
    }

    @Override
    public String getTipoQuarto() {
        return "Simples";
    }
}
