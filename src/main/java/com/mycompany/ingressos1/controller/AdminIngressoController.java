package com.mycompany.ingressos1.controller;

import com.mycompany.ingressos1.model.EstadoIngresso;
import com.mycompany.ingressos1.model.Evento;
import com.mycompany.ingressos1.model.Ingresso;
import com.mycompany.ingressos1.repository.EventoRepository;
import com.mycompany.ingressos1.repository.IngressoRepository;
import com.mycompany.ingressos1.service.IngressoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminIngressoController {

    private final EventoRepository eventoRepository;
    private final IngressoRepository ingressoRepository;
    private final IngressoService ingressoService;

    public AdminIngressoController(EventoRepository eventoRepository,
                                  IngressoRepository ingressoRepository,
                                  IngressoService ingressoService) {
        this.eventoRepository = eventoRepository;
        this.ingressoRepository = ingressoRepository;
        this.ingressoService = ingressoService;
    }

    private boolean isAdmin(HttpSession session) {
        return session.getAttribute("usuarioLogado") != null
                && "ADMIN".equals(session.getAttribute("perfilUsuario"));
    }

    @GetMapping("/eventos/{eventoId}/ingressos")
    public String ingressosPorEvento(@PathVariable String eventoId, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Evento evento = eventoRepository.findById(eventoId).orElse(null);
        if (evento == null) {
            model.addAttribute("erro", "Evento não encontrado");
            return "erro";
        }

        List<Ingresso> ingressos = ingressoRepository.findByEventoId(eventoId);

        long reservados = ingressoRepository.countByEventoIdAndEstado(eventoId, EstadoIngresso.RESERVADO);
        long pagos = ingressoRepository.countByEventoIdAndEstado(eventoId, EstadoIngresso.PAGO);
        long utilizados = ingressoRepository.countByEventoIdAndEstado(eventoId, EstadoIngresso.UTILIZADO);
        long cancelados = ingressoRepository.countByEventoIdAndEstado(eventoId, EstadoIngresso.CANCELADO);
        int disponiveis = evento.getIngressosRestantes();

        model.addAttribute("evento", evento);
        model.addAttribute("ingressos", ingressos);
        model.addAttribute("reservados", reservados);
        model.addAttribute("pagos", pagos);
        model.addAttribute("utilizados", utilizados);
        model.addAttribute("cancelados", cancelados);
        model.addAttribute("disponiveis", disponiveis);

        return "admin-ingressos";
    }

    @GetMapping("/validar")
    public String validarPorHash(@RequestParam(required = false) String hash, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        if (hash == null || hash.isBlank()) {
            model.addAttribute("info", "Informe o hash do ingresso (ou escaneie o QR Code).");
            return "admin-validar";
        }

        Ingresso ingresso = ingressoService.buscarPorHashId(hash).orElse(null);
        model.addAttribute("hash", hash);
        model.addAttribute("ingresso", ingresso);

        return "admin-validar";
    }

    @PostMapping("/validar/utilizar")
    public String validarEUtilizar(@RequestParam String hash, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        ingressoService.validarEUtilizarPorHash(hash);
        return "redirect:/admin/validar?hash=" + hash + "&sucesso=1";
    }
}
