package com.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "quartos_suite")
public class QuartoSuite extends Quarto {

    @Column(name = "taxa_luxo")
    private Double taxaLuxo = 0.3;

    public QuartoSuite() {}

    public QuartoSuite(Integer numero, Integer capacidade, Double valorDiaria, Double taxaLuxo, Hotel hotel) {
        super(numero, capacidade, valorDiaria, hotel);
        this.taxaLuxo = taxaLuxo;
    }

    @Override
    public double calcularValorTotal(int dias) {
        return getValorDiaria() * dias * (1 + taxaLuxo);
    }

    @Override
    public String getTipoQuarto() {
        return "Suíte";
    }

    public Double getTaxaLuxo() { return taxaLuxo; }
    public void setTaxaLuxo(Double taxaLuxo) { this.taxaLuxo = taxaLuxo; }
}
