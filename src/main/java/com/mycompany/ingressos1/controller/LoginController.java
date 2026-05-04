package com.mycompany.ingressos1.controller;

import com.mycompany.ingressos1.model.Usuario;
import com.mycompany.ingressos1.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String login,
                             @RequestParam String senha,
                             Model model,
                             HttpSession session) {

        Optional<Usuario> usuario = usuarioService.fazerLogin(login, senha);

        if (usuario.isPresent()) {

            session.setAttribute("usuarioLogado", usuario.get().getLogin());
            session.setAttribute("nomeUsuario", usuario.get().getNome());
            session.setAttribute("perfilUsuario", usuario.get().getPerfil().name());

            if (usuario.get().getPerfil().name().equals("ADMIN")) {
                return "redirect:/admin";
            } else {
                return "redirect:/eventos";
            }

        } else {
            model.addAttribute("erro", "Login inválido");
            return "login";
        }
    }

    @GetMapping("/cadastro-usuario")
    public String cadastroUsuario() {
        return "cadastro-usuario";
    }

    @PostMapping("/cadastro-usuario")
    public String salvarUsuario(@RequestParam String nome,
                                @RequestParam String login,
                                @RequestParam String senha) {

        usuarioService.cadastrarUsuario(nome, login, senha);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}