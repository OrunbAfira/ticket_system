/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ingressos1.repository;

import com.mycompany.ingressos1.model.Evento;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author RafaelG
 */
public interface EventoRepository extends MongoRepository<Evento, String> {
}
