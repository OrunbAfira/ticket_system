/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ingressos1.repository;

import com.mycompany.ingressos1.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

/**
 *
 * @author RafaelG
 */
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByLogin(String login);

}
