package com.mycompany.ingressos1.repository;

import com.mycompany.ingressos1.model.Ingresso;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IngressoRepository extends MongoRepository<Ingresso, String> {

    List<Ingresso> findByNomeCliente(String nomeCliente);

}