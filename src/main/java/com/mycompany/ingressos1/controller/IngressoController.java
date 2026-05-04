package com.mycompany.ingressos1.controller;

import com.mycompany.ingressos1.model.Ingresso;
import com.mycompany.ingressos1.service.IngressoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class IngressoController {

    private final IngressoService ingressoService;

    public IngressoController(IngressoService ingressoService) {
        this.ingressoService = ingressoService;
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
                                 @RequestParam String nomeEvento,
                                 @RequestParam String nomeCliente,
                                 @RequestParam String dataEvento,
                                 @RequestParam double valorBase,
                                 HttpSession session) {

        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        ingressoService.criarIngresso(tipo, nomeEvento, nomeCliente, dataEvento, valorBase);

        return "redirect:/ingressos";
    }

    @GetMapping("/ingressos")
    public String listarIngressos(Model model, HttpSession session) {

        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        String perfil = (String) session.getAttribute("perfilUsuario");
        String nomeUsuario = (String) session.getAttribute("nomeUsuario");

        List<Ingresso> ingressos;

        if ("ADMIN".equals(perfil)) {
            ingressos = ingressoService.listarTodos();
        } else {
            ingressos = ingressoService.listarPorCliente(nomeUsuario);
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

        ingressoService.utilizarIngresso(id);
        return "redirect:/ingressos";
    }

    @GetMapping("/ingressos/deletar/{id}")
    public String deletarIngresso(@PathVariable String id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        ingressoService.deletar(id);
        return "redirect:/ingressos";
    }
}