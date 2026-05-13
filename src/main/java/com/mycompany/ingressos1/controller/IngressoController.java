package com.mycompany.ingressos1.controller;

import com.mycompany.ingressos1.model.Ingresso;
import com.mycompany.ingressos1.service.IngressoService;
import com.mycompany.ingressos1.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class IngressoController {

    private final IngressoService ingressoService;
    private final UsuarioService usuarioService;

    public IngressoController(IngressoService ingressoService, UsuarioService usuarioService) {
        this.ingressoService = ingressoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/ingressos/novo")
    public String formularioCadastro(HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        return "cadastro";
    }

    @PostMapping("/ingressos/salvar")
    public String salvarIngresso(@RequestParam String tipo,
                                 @RequestParam String eventoId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        String login = (String) session.getAttribute("usuarioLogado");
        if (login == null) {
            return "redirect:/login";
        }

        if (eventoId == null || eventoId.isBlank()) {
            return "redirect:/eventos?erro=Evento inválido";
        }

        try {
            usuarioService.buscarPorLogin(login)
                .ifPresentOrElse(
                    usuario -> ingressoService.reservarIngressoParaEvento(tipo, eventoId, usuario),
                    () -> {
                        throw new IllegalStateException("Usuário não encontrado na sessão");
                    }
                );
        } catch (IllegalStateException e) {
            redirectAttributes.addAttribute("erro", e.getMessage());
            return "redirect:/comprar/" + eventoId;
        }

        return "redirect:/ingressos";
    }

    @GetMapping("/ingressos")
    public String listarIngressos(Model model, HttpSession session) {

        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        String perfil = (String) session.getAttribute("perfilUsuario");
        String nomeUsuario = (String) session.getAttribute("nomeUsuario");
        String login = (String) session.getAttribute("usuarioLogado");

        List<Ingresso> ingressos;

        if ("ADMIN".equals(perfil)) {
            ingressos = ingressoService.listarTodos();
        } else {
            ingressos = ingressoService.listarPorLogin(login, nomeUsuario);
        }

        model.addAttribute("ingressos", ingressos);
        return "lista";
    }

    @GetMapping("/ingressos/{id}")
    public String detalhesIngresso(@PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        Ingresso ingresso = ingressoService.buscarPorId(id).orElse(null);
        model.addAttribute("ingresso", ingresso);
        return "detalhes";
    }

    @GetMapping("/ingressos/pagar/{id}")
    public String pagarIngresso(@PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        Ingresso ingresso = ingressoService.pagarERetornarIngresso(id).orElse(null);
        model.addAttribute("ingresso", ingresso);
        return "imprimir-ingresso";
    }

    @GetMapping("/ingressos/cancelar/{id}")
    public String cancelarIngresso(@PathVariable String id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        ingressoService.cancelarIngresso(id);
        return "redirect:/ingressos";
    }

    @GetMapping("/ingressos/utilizar/{id}")
    public String utilizarIngresso(@PathVariable String id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        String perfil = (String) session.getAttribute("perfilUsuario");
        if (!"ADMIN".equals(perfil)) {
            return "redirect:/ingressos?erro=Ação permitida apenas para administrador";
        }

        ingressoService.utilizarIngresso(id);
        return "redirect:/ingressos";
    }

    @GetMapping("/ingressos/deletar/{id}")
    public String deletarIngresso(@PathVariable String id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        String perfil = (String) session.getAttribute("perfilUsuario");
        if (!"ADMIN".equals(perfil)) {
            return "redirect:/ingressos?erro=Ação permitida apenas para administrador";
        }

        ingressoService.deletar(id);
        return "redirect:/ingressos";
    }
}