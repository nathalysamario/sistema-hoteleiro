package com.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "funcionarios")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true)
    private String cpf;

    private String cargo;

    private Double salario;

    private LocalDate dataContratacao;

    private String horarioTrabalho;

    private LocalDate inicioFerias;

    private LocalDate fimFerias;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonIgnoreProperties({"quartos", "funcionarios"})
    private Hotel hotel;

    public Funcionario() {}

    public Funcionario(String nome, String cpf, String cargo, Double salario, LocalDate dataContratacao, Hotel hotel) {
        this.nome = nome;
        this.cpf = cpf;
        this.cargo = cargo;
        this.salario = salario;
        this.dataContratacao = dataContratacao;
        this.hotel = hotel;
    }

    public abstract String getTipoCargo();

    public String getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public Double getSalario() { return salario; }
    public void setSalario(Double salario) { this.salario = salario; }
    public LocalDate getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(LocalDate dataContratacao) { this.dataContratacao = dataContratacao; }
    public String getHorarioTrabalho() { return horarioTrabalho; }
    public void setHorarioTrabalho(String horarioTrabalho) { this.horarioTrabalho = horarioTrabalho; }
    public LocalDate getInicioFerias() { return inicioFerias; }
    public void setInicioFerias(LocalDate inicioFerias) { this.inicioFerias = inicioFerias; }
    public LocalDate getFimFerias() { return fimFerias; }
    public void setFimFerias(LocalDate fimFerias) { this.fimFerias = fimFerias; }
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
}
