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
            session.setAttribute("usuarioId", usuario.get().getId() != null ? usuario.get().getId() : usuario.get().getLogin());

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
                                @RequestParam String senha,
                                Model model) {

        try {
            // Validar campos vazios
            if (nome == null || nome.trim().isEmpty()) {
                model.addAttribute("erro", "Nome não pode estar vazio");
                return "cadastro-usuario";
            }
            if (login == null || login.trim().isEmpty()) {
                model.addAttribute("erro", "Usuário não pode estar vazio");
                return "cadastro-usuario";
            }
            if (senha == null || senha.trim().isEmpty()) {
                model.addAttribute("erro", "Senha não pode estar vazia");
                return "cadastro-usuario";
            }

            // Verificar se o login já existe
            if (usuarioService.verificarLoginExistente(login)) {
                model.addAttribute("erro", "Este usuário já está registrado");
                return "cadastro-usuario";
            }

            usuarioService.cadastrarUsuario(nome, login, senha);
            model.addAttribute("sucesso", "Usuário registrado com sucesso! Faça login para continuar.");
            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao registrar: " + e.getMessage());
            return "cadastro-usuario";
        }
    }

    @GetMapping("/logout")
    public String logoutGet(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutPost(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}