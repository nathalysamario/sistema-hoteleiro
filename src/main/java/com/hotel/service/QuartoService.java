package com.hotel.service;

import com.hotel.exception.HotelException;
import com.hotel.exception.RecursoNaoEncontradoException;
import com.hotel.model.Hotel;
import com.hotel.model.Quarto;
import com.hotel.model.SituacaoQuarto;
import com.hotel.repository.QuartoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuartoService {

    private final QuartoRepository repository;
    private final HotelService hotelService;

    public QuartoService(QuartoRepository repository, HotelService hotelService) {
        this.repository = repository;
        this.hotelService = hotelService;
    }

    public List<Quarto> listarTodos() { return repository.findAll(); }
    public List<Quarto> listarPorHotel(String hotelId) { return repository.findByHotelId(hotelId); }
    public List<Quarto> listarDisponiveis(String hotelId) { return repository.findByHotelIdAndSituacao(hotelId, SituacaoQuarto.DISPONIVEL); }

    public Quarto buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quarto", id));
    }

    public Quarto salvar(Quarto quarto) {
        Hotel hotel = hotelService.buscarPorId(quarto.getHotel().getId());
        if (repository.existsByNumeroAndHotelId(quarto.getNumero(), hotel.getId())) {
            throw new HotelException("Já existe um quarto com o número " + quarto.getNumero() + " neste hotel.");
        }
        quarto.setHotel(hotel);
        return repository.save(quarto);
    }

    public Quarto atualizar(String id, Quarto dados) {
        Quarto quarto = buscarPorId(id);
        quarto.setCapacidade(dados.getCapacidade());
        quarto.setValorDiaria(dados.getValorDiaria());
        quarto.setSituacao(dados.getSituacao());
        return repository.save(quarto);
    }

    public void deletar(String id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    public Quarto atualizarSituacao(String id, SituacaoQuarto situacao) {
        Quarto quarto = buscarPorId(id);
        quarto.setSituacao(situacao);
        return repository.save(quarto);
    }
}
