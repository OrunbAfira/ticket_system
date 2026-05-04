package com.mycompany.ingressos1.controller;

import com.mycompany.ingressos1.model.Evento;
import com.mycompany.ingressos1.service.EventoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/admin")
    public String painelAdmin(Model model) {
        model.addAttribute("eventos", eventoService.listar());
        return "admin";
    }

    @GetMapping("/admin/novo")
    public String novoEvento() {
        return "cadastrar-evento";
    }

    @PostMapping("/admin/salvar")
    public String salvarEvento(@RequestParam String nome,
                               @RequestParam String data,
                               @RequestParam String local,
                               @RequestParam double valorBase) {

        Evento evento = new Evento(nome, data, local, valorBase);
        eventoService.salvar(evento);

        return "redirect:/admin";
    }

    @GetMapping("/eventos")
    public String listarEventosParaUsuario(Model model) {
        model.addAttribute("eventos", eventoService.listar());
        return "eventos";
    }

    @GetMapping("/eventos/comprar/{id}")
    public String comprarEvento(@PathVariable String id, Model model) {
        Evento evento = eventoService.buscarPorId(id).orElse(null);
        model.addAttribute("evento", evento);
        return "comprar-evento";
    }
}