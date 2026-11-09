package com.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quarto_id", nullable = false)
    @JsonIgnoreProperties({"quartos", "funcionarios", "hotel"})
    private Quarto quarto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospede_id", nullable = false)
    @JsonIgnoreProperties({"reservas"})
    private Hospede hospede;

    @Column(nullable = false)
    private LocalDate dataEntrada;

    @Column(nullable = false)
    private LocalDate dataSaida;

    @Enumerated(EnumType.STRING)
    private SituacaoReserva situacao = SituacaoReserva.PENDENTE;

    private Double valorTotal;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "reserva_servicos",
        joinColumns = @JoinColumn(name = "reserva_id"),
        inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private List<Servico> servicos = new ArrayList<>();

    public Reserva() {}

    public Reserva(Quarto quarto, Hospede hospede, LocalDate dataEntrada, LocalDate dataSaida) {
        this.quarto = quarto;
        this.hospede = hospede;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }

    public int calcularDias() {
        return (int) ChronoUnit.DAYS.between(dataEntrada, dataSaida);
    }

    public double calcularValorTotal() {
        int dias = calcularDias();
        double totalQuarto = quarto.calcularValorTotal(dias);
        double totalServicos = servicos.stream().mapToDouble(Servico::getPreco).sum();
        return totalQuarto + totalServicos;
    }

    public void adicionarServico(Servico servico) {
        this.servicos.add(servico);
        this.valorTotal = calcularValorTotal();
    }

    public String getId() { return id; }
    public Quarto getQuarto() { return quarto; }
    public void setQuarto(Quarto quarto) { this.quarto = quarto; }
    public Hospede getHospede() { return hospede; }
    public void setHospede(Hospede hospede) { this.hospede = hospede; }
    public LocalDate getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }
    public LocalDate getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDate dataSaida) { this.dataSaida = dataSaida; }
    public SituacaoReserva getSituacao() { return situacao; }
    public void setSituacao(SituacaoReserva situacao) { this.situacao = situacao; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public List<Servico> getServicos() { return servicos; }
}
