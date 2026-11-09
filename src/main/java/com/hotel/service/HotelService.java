package com.hotel.service;

import com.hotel.exception.RecursoNaoEncontradoException;
import com.hotel.model.Hotel;
import com.hotel.repository.HotelRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelService {

    private final HotelRepository repository;

    public HotelService(HotelRepository repository) {
        this.repository = repository;
    }

    public List<Hotel> listarTodos() {
        return repository.findAll();
    }

    public Hotel buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Hotel", id));
    }

    public Hotel salvar(Hotel hotel) {
        return repository.save(hotel);
    }

    public Hotel atualizar(String id, Hotel dados) {
        Hotel hotel = buscarPorId(id);
        hotel.setNome(dados.getNome());
        hotel.setEndereco(dados.getEndereco());
        hotel.setTelefone(dados.getTelefone());
        hotel.setEmail(dados.getEmail());
        return repository.save(hotel);
    }

    public void deletar(String id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    public List<Hotel> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }
}
