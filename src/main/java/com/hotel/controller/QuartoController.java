package com.hotel.controller;

import com.hotel.model.Quarto;
import com.hotel.model.QuartoSimples;
import com.hotel.model.QuartoSuite;
import com.hotel.model.SituacaoQuarto;
import com.hotel.service.QuartoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quartos")
public class QuartoController {

    private final QuartoService service;

    public QuartoController(QuartoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Quarto> listar(@RequestParam(required = false) String hotelId,
                               @RequestParam(required = false) Boolean disponiveis) {
        if (hotelId != null && Boolean.TRUE.equals(disponiveis)) return service.listarDisponiveis(hotelId);
        if (hotelId != null) return service.listarPorHotel(hotelId);
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Quarto buscar(@PathVariable String id) {
        return service.buscarPorId(id);
    }

    @PostMapping("/simples")
    public ResponseEntity<Quarto> criarSimples(@RequestBody QuartoSimples quarto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(quarto));
    }

    @PostMapping("/suite")
    public ResponseEntity<Quarto> criarSuite(@RequestBody QuartoSuite quarto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(quarto));
    }

    @PutMapping("/{id}")
    public Quarto atualizar(@PathVariable String id, @RequestBody Quarto quarto) {
        return service.atualizar(id, quarto);
    }

    @PatchMapping("/{id}/situacao")
    public Quarto atualizarSituacao(@PathVariable String id, @RequestBody Map<String, String> body) {
        SituacaoQuarto situacao = SituacaoQuarto.valueOf(body.get("situacao"));
        return service.atualizarSituacao(id, situacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
