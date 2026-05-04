/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ingressos1.service;

import com.mycompany.ingressos1.model.PerfilUsuario;
import com.mycompany.ingressos1.model.Usuario;
import com.mycompany.ingressos1.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 * @author RafaelG
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrarUsuario(String nome, String login, String senha) {
        Usuario usuario = new Usuario(nome, login, senha, PerfilUsuario.USUARIO);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> fazerLogin(String login, String senha) {

        if (login.equals("admin") && senha.equals("123")) {
            Usuario admin = new Usuario("Administrador", "admin", "123", PerfilUsuario.ADMIN);
            return Optional.of(admin);
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findByLogin(login);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            if (usuario.getSenha().equals(senha)) {
                return Optional.of(usuario);
            }
        }

        return Optional.empty();
    }
}