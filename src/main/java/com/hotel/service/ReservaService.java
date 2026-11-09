package com.hotel.service;

import com.hotel.exception.HotelException;
import com.hotel.exception.RecursoNaoEncontradoException;
import com.hotel.model.*;
import com.hotel.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository repository;
    private final QuartoService quartoService;
    private final HospedeService hospedeService;
    private final ServicoService servicoService;

    public ReservaService(ReservaRepository repository, QuartoService quartoService,
                          HospedeService hospedeService, ServicoService servicoService) {
        this.repository = repository;
        this.quartoService = quartoService;
        this.hospedeService = hospedeService;
        this.servicoService = servicoService;
    }

    public List<Reserva> listarTodas() { return repository.findAll(); }
    public List<Reserva> listarPorHospede(String hospedeId) { return repository.findByHospedeId(hospedeId); }

    public Reserva buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva", id));
    }

    public Reserva criar(String quartoId, String hospedeId, LocalDate dataEntrada, LocalDate dataSaida) {
        if (!dataSaida.isAfter(dataEntrada)) {
            throw new HotelException("A data de saída deve ser posterior à data de entrada.");
        }
        Quarto quarto = quartoService.buscarPorId(quartoId);
        Hospede hospede = hospedeService.buscarPorId(hospedeId);

        if (quarto.getSituacao() == SituacaoQuarto.MANUTENCAO) {
            throw new HotelException("O quarto está em manutenção e não pode ser reservado.");
        }
        List<Reserva> conflitos = repository.findConflitos(quartoId, dataEntrada, dataSaida);
        if (!conflitos.isEmpty()) {
            throw new HotelException("O quarto já possui uma reserva para o período informado.");
        }
        return repository.save(new Reserva(quarto, hospede, dataEntrada, dataSaida));
    }

    public Reserva confirmar(String id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getSituacao() != SituacaoReserva.PENDENTE) {
            throw new HotelException("Só é possível confirmar reservas com situação PENDENTE.");
        }
        reserva.setSituacao(SituacaoReserva.CONFIRMADA);
        return repository.save(reserva);
    }

    public Reserva checkIn(String id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getSituacao() != SituacaoReserva.CONFIRMADA && reserva.getSituacao() != SituacaoReserva.PENDENTE) {
            throw new HotelException("Check-in só pode ser feito em reservas PENDENTES ou CONFIRMADAS.");
        }
        reserva.setSituacao(SituacaoReserva.CONFIRMADA);
        quartoService.atualizarSituacao(reserva.getQuarto().getId(), SituacaoQuarto.OCUPADO);
        return repository.save(reserva);
    }

    public Reserva checkOut(String id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getSituacao() != SituacaoReserva.CONFIRMADA) {
            throw new HotelException("Check-out só pode ser feito em reservas CONFIRMADAS.");
        }
        reserva.setValorTotal(reserva.calcularValorTotal());
        reserva.setSituacao(SituacaoReserva.FINALIZADA);
        quartoService.atualizarSituacao(reserva.getQuarto().getId(), SituacaoQuarto.DISPONIVEL);
        return repository.save(reserva);
    }

    public Reserva cancelar(String id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getSituacao() == SituacaoReserva.FINALIZADA) {
            throw new HotelException("Não é possível cancelar uma reserva já finalizada.");
        }
        reserva.setSituacao(SituacaoReserva.CANCELADA);
        if (reserva.getQuarto().getSituacao() == SituacaoQuarto.OCUPADO) {
            quartoService.atualizarSituacao(reserva.getQuarto().getId(), SituacaoQuarto.DISPONIVEL);
        }
        return repository.save(reserva);
    }

    public Reserva adicionarServico(String reservaId, String servicoId) {
        Reserva reserva = buscarPorId(reservaId);
        Servico servico = servicoService.buscarPorId(servicoId);
        reserva.adicionarServico(servico);
        return repository.save(reserva);
    }
}
