package com.hotel.controller;

import com.hotel.model.Funcionario;
import com.hotel.model.Gerente;
import com.hotel.model.Recepcionista;
import com.hotel.service.FuncionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Funcionario> listar(@RequestParam(required = false) String hotelId,
                                    @RequestParam(required = false) String nome) {
        if (hotelId != null) return service.listarPorHotel(hotelId);
        if (nome != null && !nome.isBlank()) return service.buscarPorNome(nome);
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Funcionario buscar(@PathVariable String id) {
        return service.buscarPorId(id);
    }

    @PostMapping("/recepcionista")
    public ResponseEntity<Funcionario> criarRecepcionista(@RequestBody Recepcionista f) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(f));
    }

    @PostMapping("/gerente")
    public ResponseEntity<Funcionario> criarGerente(@RequestBody Gerente f) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(f));
    }

    @PutMapping("/{id}")
    public Funcionario atualizar(@PathVariable String id, @RequestBody Funcionario funcionario) {
        return service.atualizar(id, funcionario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
