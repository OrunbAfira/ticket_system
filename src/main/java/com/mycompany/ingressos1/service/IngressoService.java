package com.mycompany.ingressos1.service;

import com.mycompany.ingressos1.model.*;
import com.mycompany.ingressos1.repository.IngressoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class IngressoService {

    private final IngressoRepository ingressoRepository;

    public IngressoService(IngressoRepository ingressoRepository) {
        this.ingressoRepository = ingressoRepository;
    }

    public Ingresso criarIngresso(String tipo, String nomeEvento, String nomeCliente,
                                  String dataEvento, double valorBase) {

        Ingresso ingresso;

        if (tipo.equalsIgnoreCase("VIP")) {
            ingresso = new IngressoVIP(nomeEvento, nomeCliente, dataEvento, valorBase);
        } else if (tipo.equalsIgnoreCase("MEIA")) {
            ingresso = new IngressoMeia(nomeEvento, nomeCliente, dataEvento, valorBase);
        } else {
            ingresso = new IngressoNormal(nomeEvento, nomeCliente, dataEvento, valorBase);
        }

        double valorFinal = ingresso.calcularValor();
        ingresso.setValorFinal(valorFinal);

        return ingressoRepository.save(ingresso);
    }

    public List<Ingresso> listarTodos() {
        return ingressoRepository.findAll();
    }

    public List<Ingresso> listarPorCliente(String nomeCliente) {
        return ingressoRepository.findByNomeCliente(nomeCliente);
    }

    public Optional<Ingresso> buscarPorId(String id) {
        return ingressoRepository.findById(id);
    }

    public Optional<Ingresso> pagarERetornarIngresso(String id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);

        if (ingressoOptional.isPresent()) {
            Ingresso ingresso = ingressoOptional.get();
            ingresso.setEstado(EstadoIngresso.PAGO);
            ingressoRepository.save(ingresso);
            return Optional.of(ingresso);
        }

        return Optional.empty();
    }

    public void pagarIngresso(String id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);

        if (ingressoOptional.isPresent()) {
            Ingresso ingresso = ingressoOptional.get();
            ingresso.setEstado(EstadoIngresso.PAGO);
            ingressoRepository.save(ingresso);
        }
    }

    public void cancelarIngresso(String id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);

        if (ingressoOptional.isPresent()) {
            Ingresso ingresso = ingressoOptional.get();
            ingresso.setEstado(EstadoIngresso.CANCELADO);
            ingressoRepository.save(ingresso);
        }
    }

    public void utilizarIngresso(String id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);

        if (ingressoOptional.isPresent()) {
            Ingresso ingresso = ingressoOptional.get();
            ingresso.setEstado(EstadoIngresso.UTILIZADO);
            ingressoRepository.save(ingresso);
        }
    }

    public void deletar(String id) {
        ingressoRepository.deleteById(id);
    }
}