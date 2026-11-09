package com.hotel.controller;

import com.hotel.model.Reserva;
import com.hotel.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reserva> listar(@RequestParam(required = false) String hospedeId) {
        if (hospedeId != null) return service.listarPorHospede(hospedeId);
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public Reserva buscar(@PathVariable String id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Reserva> criar(@RequestBody Map<String, String> body) {
        String quartoId = body.get("quartoId");
        String hospedeId = body.get("hospedeId");
        LocalDate entrada = LocalDate.parse(body.get("dataEntrada"));
        LocalDate saida = LocalDate.parse(body.get("dataSaida"));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(quartoId, hospedeId, entrada, saida));
    }

    @PutMapping("/{id}/confirmar")
    public Reserva confirmar(@PathVariable String id) {
        return service.confirmar(id);
    }

    @PutMapping("/{id}/checkin")
    public Reserva checkIn(@PathVariable String id) {
        return service.checkIn(id);
    }

    @PutMapping("/{id}/checkout")
    public Reserva checkOut(@PathVariable String id) {
        return service.checkOut(id);
    }

    @PutMapping("/{id}/cancelar")
    public Reserva cancelar(@PathVariable String id) {
        return service.cancelar(id);
    }

    @PostMapping("/{id}/servicos/{servicoId}")
    public Reserva adicionarServico(@PathVariable String id, @PathVariable String servicoId) {
        return service.adicionarServico(id, servicoId);
    }
}
