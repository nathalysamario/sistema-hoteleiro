package com.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "quartos")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Integer numero;

    private Integer capacidade;

    @Column(nullable = false)
    private Double valorDiaria;

    @Enumerated(EnumType.STRING)
    private SituacaoQuarto situacao = SituacaoQuarto.DISPONIVEL;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonIgnoreProperties({"quartos", "funcionarios"})
    private Hotel hotel;

    public Quarto() {}

    public Quarto(Integer numero, Integer capacidade, Double valorDiaria, Hotel hotel) {
        this.numero = numero;
        this.capacidade = capacidade;
        this.valorDiaria = valorDiaria;
        this.hotel = hotel;
    }

    public abstract double calcularValorTotal(int dias);
    public abstract String getTipoQuarto();

    public String getId() { return id; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }
    public Double getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(Double valorDiaria) { this.valorDiaria = valorDiaria; }
    public SituacaoQuarto getSituacao() { return situacao; }
    public void setSituacao(SituacaoQuarto situacao) { this.situacao = situacao; }
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }

    public boolean isDisponivel() { return situacao == SituacaoQuarto.DISPONIVEL; }
}
