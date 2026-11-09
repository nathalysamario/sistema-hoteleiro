package com.hotel.service;

import com.hotel.exception.RecursoNaoEncontradoException;
import com.hotel.model.Servico;
import com.hotel.repository.ServicoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository repository;

    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    public List<Servico> listarTodos() { return repository.findAll(); }

    public Servico buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço", id));
    }

    public Servico salvar(Servico servico) { return repository.save(servico); }

    public Servico atualizar(String id, Servico dados) {
        Servico servico = buscarPorId(id);
        servico.setNome(dados.getNome());
        servico.setDescricao(dados.getDescricao());
        servico.setPreco(dados.getPreco());
        servico.setTipo(dados.getTipo());
        return repository.save(servico);
    }

    public void deletar(String id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
