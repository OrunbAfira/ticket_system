package com.mycompany.ingressos1.repository;

import com.mycompany.ingressos1.model.Ingresso;
import com.mycompany.ingressos1.model.EstadoIngresso;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IngressoRepository extends MongoRepository<Ingresso, String> {

    List<Ingresso> findByNomeCliente(String nomeCliente);

    List<Ingresso> findByClienteLogin(String login);

    List<Ingresso> findByEventoId(String eventoId);

    List<Ingresso> findByClienteLoginAndEventoId(String login, String eventoId);

    List<Ingresso> findByNomeClienteAndEventoId(String nomeCliente, String eventoId);

    Optional<Ingresso> findByHashId(String hashId);

    long countByEventoIdAndEstado(String eventoId, EstadoIngresso estado);

}
