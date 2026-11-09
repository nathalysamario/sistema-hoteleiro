package com.hotel.service;

import com.hotel.exception.HotelException;
import com.hotel.exception.RecursoNaoEncontradoException;
import com.hotel.model.Hospede;
import com.hotel.repository.HospedeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HospedeService {

    private final HospedeRepository repository;

    public HospedeService(HospedeRepository repository) {
        this.repository = repository;
    }

    public List<Hospede> listarTodos() { return repository.findAll(); }
    public List<Hospede> buscarPorNome(String nome) { return repository.findByNomeContainingIgnoreCase(nome); }

    public Hospede buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Hóspede", id));
    }

    public Hospede salvar(Hospede hospede) {
        repository.findByCpf(hospede.getCpf()).ifPresent(h -> {
            throw new HotelException("Já existe um hóspede com o CPF " + hospede.getCpf());
        });
        return repository.save(hospede);
    }

    public Hospede atualizar(String id, Hospede dados) {
        Hospede hospede = buscarPorId(id);
        hospede.setNome(dados.getNome());
        hospede.setTelefone(dados.getTelefone());
        hospede.setEmail(dados.getEmail());
        return repository.save(hospede);
    }

    public void deletar(String id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
