package com.mycompany.ingressos1.service;

import com.mycompany.ingressos1.model.Evento;
import com.mycompany.ingressos1.repository.EventoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento salvar(Evento evento) {
        return eventoRepository.save(evento);
    }

    public List<Evento> listar() {
        return eventoRepository.findAll();
    }
    public Optional<Evento> buscarPorId(String id) {
    return eventoRepository.findById(id);

    }
}
