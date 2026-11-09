package com.hotel.service;

import com.hotel.exception.HotelException;
import com.hotel.exception.RecursoNaoEncontradoException;
import com.hotel.model.Funcionario;
import com.hotel.model.Hotel;
import com.hotel.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final HotelService hotelService;

    public FuncionarioService(FuncionarioRepository repository, HotelService hotelService) {
        this.repository = repository;
        this.hotelService = hotelService;
    }

    public List<Funcionario> listarTodos() { return repository.findAll(); }
    public List<Funcionario> listarPorHotel(String hotelId) { return repository.findByHotelId(hotelId); }
    public List<Funcionario> buscarPorNome(String nome) { return repository.findByNomeContainingIgnoreCase(nome); }

    public Funcionario buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário", id));
    }

    public Funcionario salvar(Funcionario funcionario) {
        repository.findByCpf(funcionario.getCpf()).ifPresent(f -> {
            throw new HotelException("Já existe um funcionário com o CPF " + funcionario.getCpf());
        });
        Hotel hotel = hotelService.buscarPorId(funcionario.getHotel().getId());
        funcionario.setHotel(hotel);
        return repository.save(funcionario);
    }

    public Funcionario atualizar(String id, Funcionario dados) {
        Funcionario funcionario = buscarPorId(id);
        funcionario.setNome(dados.getNome());
        funcionario.setSalario(dados.getSalario());
        funcionario.setHorarioTrabalho(dados.getHorarioTrabalho());
        funcionario.setInicioFerias(dados.getInicioFerias());
        funcionario.setFimFerias(dados.getFimFerias());
        return repository.save(funcionario);
    }

    public void deletar(String id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
