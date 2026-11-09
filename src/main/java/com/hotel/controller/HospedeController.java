package com.hotel.controller;

import com.hotel.model.Hospede;
import com.hotel.service.HospedeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospedes")
public class HospedeController {

    private final HospedeService service;

    public HospedeController(HospedeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Hospede> listar(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isBlank()) return service.buscarPorNome(nome);
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Hospede buscar(@PathVariable String id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Hospede> criar(@RequestBody Hospede hospede) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(hospede));
    }

    @PutMapping("/{id}")
    public Hospede atualizar(@PathVariable String id, @RequestBody Hospede hospede) {
        return service.atualizar(id, hospede);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
