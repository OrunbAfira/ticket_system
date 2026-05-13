package com.mycompany.ingressos1.service;

import com.mycompany.ingressos1.model.*;
import com.mycompany.ingressos1.repository.EventoRepository;
import com.mycompany.ingressos1.repository.IngressoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngressoService {

    private final IngressoRepository ingressoRepository;
    private final EventoRepository eventoRepository;

    public IngressoService(IngressoRepository ingressoRepository, EventoRepository eventoRepository) {
        this.ingressoRepository = ingressoRepository;
        this.eventoRepository = eventoRepository;
    }

    public Ingresso criarIngresso(String tipo, String nomeEvento, String nomeCliente,
                                  String dataEvento, double valorBase) {

        Ingresso ingresso;

        if (tipo.equalsIgnoreCase("VIP")) {
            ingresso = new IngressoVIP(nomeEvento, nomeCliente, dataEvento, valorBase);
        } else if (tipo.equalsIgnoreCase("MEIA")) {
            ingresso = new IngressoMeia(nomeEvento, nomeCliente, dataEvento, valorBase);
        } else {
            ingresso = new IngressoNormal(nomeEvento, nomeCliente, dataEvento, valorBase);
        }

        double valorFinal = ingresso.calcularValor();
        ingresso.setValorFinal(valorFinal);
        
        // O hash é gerado automaticamente no construtor, 
        // mas aqui garantimos que está pronto antes de salvar
        String hashId = ingresso.getHashId();
        if (hashId == null) {
            ingresso.setHashId(ingresso.gerarHashId());
        }

        return ingressoRepository.save(ingresso);
    }

    public Ingresso criarIngressoParaUsuario(String tipo,
                                             String nomeEvento,
                                             Usuario usuario,
                                             String dataEvento,
                                             double valorBase) {

        Ingresso ingresso = criarIngresso(tipo, nomeEvento, usuario.getNome(), dataEvento, valorBase);
        ingresso.setCliente(new UsuarioRef(usuario.getId(), usuario.getLogin(), usuario.getNome()));
        return ingressoRepository.save(ingresso);
    }

    /**
     * Reserva/compra um ingresso a partir de um evento existente.
     * Regras aplicadas:
     * - não permite se o evento estiver lotado
     * - associa o ingresso a um cliente autenticado
     * - impede duplicidade indevida (mesmo usuário + mesmo evento com reserva ativa)
     * - atualiza automaticamente a quantidade de ingressos disponíveis (incrementa vendidos)
     */
    public Ingresso reservarIngressoParaEvento(String tipo, String eventoId, Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário autenticado é obrigatório");
        }
        if (eventoId == null || eventoId.isBlank()) {
            throw new IllegalArgumentException("Evento inválido");
        }

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        if (!evento.temIngressosDisponiveis()) {
            throw new IllegalStateException("Não há ingressos disponíveis para este evento");
        }

        // Impedir duplicidade indevida: já existe ingresso ativo do mesmo usuário para o mesmo evento
        List<Ingresso> existentes = ingressoRepository.findByClienteLoginAndEventoId(usuario.getLogin(), eventoId);
        boolean temAtivo = existentes.stream().anyMatch(i -> {
            EstadoIngresso estado = i.getEstado();
            return estado != null && estado != EstadoIngresso.CANCELADO;
        });
        if (temAtivo) {
            throw new IllegalStateException("Você já possui uma reserva/ingresso ativo para este evento");
        }

        // Atualizar estoque (vendidos) no evento
        boolean vendeu = evento.venderIngressos(1);
        if (!vendeu) {
            throw new IllegalStateException("Não foi possível reservar: evento sem ingressos");
        }
        eventoRepository.save(evento);

        Ingresso ingresso = criarIngressoParaUsuario(
                tipo,
                evento.getNome(),
                usuario,
                evento.getDataHoraFormatada(),
                evento.getValorBase()
        );
        ingresso.setEventoId(evento.getId());
        return ingressoRepository.save(ingresso);
    }

    public List<Ingresso> listarTodos() {
        return ingressoRepository.findAll();
    }

    public List<Ingresso> listarPorCliente(String nomeCliente) {
        return ingressoRepository.findByNomeCliente(nomeCliente);
    }

    public List<Ingresso> listarPorLogin(String login, String nomeFallback) {
        List<Ingresso> porLogin = ingressoRepository.findByClienteLogin(login);
        if (porLogin != null && !porLogin.isEmpty()) {
            return porLogin;
        }
        // Fallback para ingressos antigos que só tinham nomeCliente
        return ingressoRepository.findByNomeCliente(nomeFallback);
    }

    public Optional<Ingresso> buscarPorId(String id) {
        return ingressoRepository.findById(id);
    }

    public Optional<Ingresso> pagarERetornarIngresso(String id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);

        if (ingressoOptional.isPresent()) {
            Ingresso ingresso = ingressoOptional.get();
            if (ingresso.getEstado() == EstadoIngresso.CANCELADO) {
                throw new IllegalStateException("Ingresso cancelado não pode ser pago");
            }
            if (ingresso.getEstado() == EstadoIngresso.UTILIZADO) {
                throw new IllegalStateException("Ingresso utilizado não pode ser pago");
            }

            ingresso.setEstado(EstadoIngresso.PAGO);
            ingressoRepository.save(ingresso);
            return Optional.of(ingresso);
        }

        return Optional.empty();
    }

    public void pagarIngresso(String id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);

        if (ingressoOptional.isPresent()) {
            Ingresso ingresso = ingressoOptional.get();
            if (ingresso.getEstado() == EstadoIngresso.CANCELADO || ingresso.getEstado() == EstadoIngresso.UTILIZADO) {
                throw new IllegalStateException("Ingresso não pode ser pago neste estado");
            }
            ingresso.setEstado(EstadoIngresso.PAGO);
            ingressoRepository.save(ingresso);
        }
    }

    public void cancelarIngresso(String id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);

        if (ingressoOptional.isPresent()) {
            Ingresso ingresso = ingressoOptional.get();
            if (ingresso.getEstado() == EstadoIngresso.UTILIZADO) {
                throw new IllegalStateException("Ingresso utilizado não pode ser cancelado");
            }
            ingresso.setEstado(EstadoIngresso.CANCELADO);
            ingressoRepository.save(ingresso);
        }
    }

    public void utilizarIngresso(String id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);

        if (ingressoOptional.isPresent()) {
            Ingresso ingresso = ingressoOptional.get();
            if (ingresso.getEstado() == EstadoIngresso.CANCELADO) {
                throw new IllegalStateException("Ingresso cancelado não pode ser utilizado");
            }
            if (ingresso.getEstado() != EstadoIngresso.PAGO) {
                throw new IllegalStateException("Somente ingressos pagos podem ser utilizados");
            }
            ingresso.setEstado(EstadoIngresso.UTILIZADO);
            ingressoRepository.save(ingresso);
        }
    }

    public Optional<Ingresso> buscarPorHashId(String hashId) {
        if (hashId == null || hashId.isBlank()) {
            return Optional.empty();
        }
        return ingressoRepository.findByHashId(hashId);
    }

    public Ingresso validarEUtilizarPorHash(String hashId) {
        Ingresso ingresso = buscarPorHashId(hashId)
                .orElseThrow(() -> new IllegalArgumentException("Ingresso não encontrado para este QR Code"));

        if (ingresso.getEstado() == EstadoIngresso.UTILIZADO) {
            throw new IllegalStateException("Ingresso já foi utilizado");
        }
        if (ingresso.getEstado() == EstadoIngresso.CANCELADO) {
            throw new IllegalStateException("Ingresso está cancelado");
        }
        if (ingresso.getEstado() != EstadoIngresso.PAGO) {
            throw new IllegalStateException("Ingresso ainda não está confirmado (pago)");
        }

        ingresso.setEstado(EstadoIngresso.UTILIZADO);
        return ingressoRepository.save(ingresso);
    }

    public void deletar(String id) {
        ingressoRepository.deleteById(id);
    }
}
