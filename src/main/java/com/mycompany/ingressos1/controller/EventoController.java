package com.mycompany.ingressos1.controller;

import com.mycompany.ingressos1.model.Evento;
import com.mycompany.ingressos1.service.EventoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para Gerenciamento de Eventos
 * Responsável por operações administrativas e visualização de eventos
 */
@Controller
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    /**
     * GET /admin - Exibe o painel administrativo com lista de eventos
     */
    @GetMapping("/admin")
    public String painelAdmin(Model model) {
        try {
            model.addAttribute("eventos", eventoService.listar());
            return "admin";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar painel administrativo: " + e.getMessage());
            return "erro";
        }
    }

    /**
     * GET /admin/novo - Exibe formulário para criar novo evento
     */
    @GetMapping("/admin/novo")
    public String novoEvento() {
        return "cadastrar-evento";
    }

    /**
     * POST /admin/salvar - Salva um novo evento no banco de dados
     * Agora aceita todos os novos atributos do evento (descrição, horário, quantidade)
     */
    @PostMapping("/admin/salvar")
    public String salvarEvento(@RequestParam String nome,
                               @RequestParam(required = false) String descricao,
                               @RequestParam String data,
                               @RequestParam(required = false) String horario,
                               @RequestParam String local,
                               @RequestParam(required = false, defaultValue = "100") int quantidadeIngressos,
                               @RequestParam double valorBase) {

        try {
            // Criar novo evento com todos os parâmetros
            Evento evento = new Evento(
                nome,
                descricao != null ? descricao : "",
                data,
                horario != null ? horario : "00:00",
                local,
                quantidadeIngressos,
                valorBase
            );

            eventoService.salvar(evento);
            return "redirect:/admin";
        } catch (Exception e) {
            // Em caso de erro, redirecionar com mensagem
            return "redirect:/admin?erro=" + e.getMessage();
        }
    }

    /**
     * GET /eventos - Lista todos os eventos disponíveis para usuários
     */
    @GetMapping("/eventos")
    public String listarEventosParaUsuario(Model model) {
        try {
            model.addAttribute("eventos", eventoService.listar());
            return "eventos";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao listar eventos: " + e.getMessage());
            return "erro";
        }
    }

    /**
     * GET /eventos/comprar/{id} - Exibe detalhes do evento para compra
     */
    @GetMapping("/eventos/comprar/{id}")
    public String comprarEvento(@PathVariable String id, Model model) {
        try {
            Evento evento = eventoService.buscarPorId(id).orElse(null);
            if (evento == null) {
                model.addAttribute("erro", "Evento não encontrado");
                return "erro";
            }
            model.addAttribute("evento", evento);
            return "comprar-evento";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao buscar evento: " + e.getMessage());
            return "erro";
        }
    }

    /**
     * GET /comprar/{id} - Endpoint alternativo para comprar ingresso
     */
    @GetMapping("/comprar/{id}")
    public String comprarIngressoAlternativo(@PathVariable String id, Model model) {
        return comprarEvento(id, model);
    }
}