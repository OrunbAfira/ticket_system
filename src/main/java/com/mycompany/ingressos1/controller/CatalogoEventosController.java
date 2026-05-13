package com.mycompany.ingressos1.controller;

import com.mycompany.ingressos1.model.Evento;
import com.mycompany.ingressos1.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

/**
 * Controller para Catálogo de Eventos
 * 
 * Responsável por exibir os eventos disponíveis no sistema
 * Permite visualizar a lista completa de eventos e detalhes específicos
 */
@Controller
@RequestMapping("/catalogo")
public class CatalogoEventosController {

    @Autowired
    private EventoRepository eventoRepository;

    /**
     * Exibe o catálogo completo de eventos
     * GET /catalogo
     * 
     * @param model modelo para passar dados ao template
     * @return página do catálogo
     */
    @GetMapping
    public String exibirCatalogo(Model model) {
        try {
            // Buscar todos os eventos
            List<Evento> eventos = eventoRepository.findAll();
            
            // Separar eventos disponíveis dos lotados
            List<Evento> eventosDisponiveis = eventos.stream()
                    .filter(e -> !e.estaLotado())
                    .toList();
            
            List<Evento> eventosLotados = eventos.stream()
                    .filter(e -> e.estaLotado())
                    .toList();
            
            // Calcular estatísticas
            long totalIngressosDisponiveis = eventosDisponiveis.stream()
                    .mapToLong(e -> e.getIngressosRestantes())
                    .sum();
            
            // Adicionar dados ao modelo
            model.addAttribute("eventos", eventosDisponiveis);
            model.addAttribute("eventosLotados", eventosLotados);
            model.addAttribute("totalEventos", eventos.size());
            model.addAttribute("totalIngressosDisponiveis", totalIngressosDisponiveis);
            
            return "catalogo-eventos";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar catálogo de eventos: " + e.getMessage());
            return "erro";
        }
    }

    /**
     * Exibe detalhes de um evento específico
     * GET /catalogo/detalhes/{id}
     * 
     * @param id ID do evento
     * @param model modelo para passar dados ao template
     * @return página de detalhes do evento
     */
    @GetMapping("/detalhes/{id}")
    public String exibirDetalhesEvento(@PathVariable String id, Model model) {
        try {
            Optional<Evento> eventoOpt = eventoRepository.findById(id);
            
            if (eventoOpt.isPresent()) {
                Evento evento = eventoOpt.get();
                
                // Adicionar informações calculadas
                model.addAttribute("evento", evento);
                model.addAttribute("ingressosRestantes", evento.getIngressosRestantes());
                model.addAttribute("percentualVenda", String.format("%.1f", evento.getPercentualVenda()));
                model.addAttribute("disponivel", evento.temIngressosDisponiveis());
                model.addAttribute("dataHora", evento.getDataHoraFormatada());
                
                return "catalogo-detalhes-evento";
            } else {
                model.addAttribute("erro", "Evento não encontrado");
                return "erro";
            }
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar detalhes do evento: " + e.getMessage());
            return "erro";
        }
    }

    /**
     * Exibe eventos filtrados por disponibilidade
     * GET /catalogo/disponiveis
     * 
     * @param model modelo para passar dados ao template
     * @return página com eventos disponíveis
     */
    @GetMapping("/disponiveis")
    public String exibirEventosDisponiveis(Model model) {
        try {
            List<Evento> eventos = eventoRepository.findAll();
            
            List<Evento> eventosDisponiveis = eventos.stream()
                    .filter(e -> e.temIngressosDisponiveis())
                    .toList();
            
            model.addAttribute("eventos", eventosDisponiveis);
            model.addAttribute("filtro", "Eventos com ingressos disponíveis");
            
            return "catalogo-eventos-filtrado";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao filtrar eventos: " + e.getMessage());
            return "erro";
        }
    }

    /**
     * Exibe eventos que estão lotados
     * GET /catalogo/lotados
     * 
     * @param model modelo para passar dados ao template
     * @return página com eventos lotados
     */
    @GetMapping("/lotados")
    public String exibirEventosLotados(Model model) {
        try {
            List<Evento> eventos = eventoRepository.findAll();
            
            List<Evento> eventosLotados = eventos.stream()
                    .filter(e -> e.estaLotado())
                    .toList();
            
            model.addAttribute("eventos", eventosLotados);
            model.addAttribute("filtro", "Eventos lotados");
            
            return "catalogo-eventos-filtrado";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao filtrar eventos: " + e.getMessage());
            return "erro";
        }
    }
}
