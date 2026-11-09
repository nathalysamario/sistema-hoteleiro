package com.hotel.controller;

import com.hotel.model.Hotel;
import com.hotel.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoteis")
public class HotelController {

    private final HotelService service;

    public HotelController(HotelService service) {
        this.service = service;
    }

    @GetMapping
    public List<Hotel> listar(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isBlank()) return service.buscarPorNome(nome);
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Hotel buscar(@PathVariable String id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Hotel> criar(@RequestBody Hotel hotel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(hotel));
    }

    @PutMapping("/{id}")
    public Hotel atualizar(@PathVariable String id, @RequestBody Hotel hotel) {
        return service.atualizar(id, hotel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
