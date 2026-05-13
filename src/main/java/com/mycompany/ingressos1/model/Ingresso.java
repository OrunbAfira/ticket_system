package com.mycompany.ingressos1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Classe abstrata Ingresso
 * Define o contrato para todos os tipos de ingresso do sistema
 */
@Document(collection = "ingressos")
public abstract class Ingresso {

    @Id
    private String id;

    /**
     * ID do evento (referência lógica ao documento Evento).
     * Mantido separado do nome para permitir consultas e validações seguras.
     */
    private String eventoId;

    private String nomeEvento;
    /**
     * Mantido por compatibilidade com templates/dados antigos.
     * Para vínculo OO use {@link #cliente}.
     */
    private String nomeCliente;

    /**
     * Referência OO ao usuário dono do ingresso.
     * (MongoDB salva como subdocumento dentro de Ingresso)
     */
    private UsuarioRef cliente;
    private String dataEvento;
    private double valorBase;
    private double valorFinal;
    private EstadoIngresso estado;
    
    /**
     * Hash único e seguro para o ingresso
     * Usado para identificação, códigos QR e validação
     */
    private String hashId;

    public Ingresso() {
        this.estado = EstadoIngresso.RESERVADO;
        this.hashId = gerarHashId();
    }

    public Ingresso(String nomeEvento, String nomeCliente, String dataEvento, double valorBase) {
        this.nomeEvento = nomeEvento;
        this.nomeCliente = nomeCliente;
        this.dataEvento = dataEvento;
        this.valorBase = valorBase;
        this.estado = EstadoIngresso.RESERVADO;
        this.hashId = gerarHashId();
    }

    public abstract double calcularValor();

    public abstract String imprimirIngresso();

    public abstract String getTipoIngresso();

    public String getImprimirIngresso() {
        return imprimirIngresso();
    }

    public String getId() {
        return id;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public UsuarioRef getCliente() {
        return cliente;
    }

    public void setCliente(UsuarioRef cliente) {
        this.cliente = cliente;
        // Mantém coerência para telas legadas
        if (cliente != null && cliente.getNome() != null) {
            this.nomeCliente = cliente.getNome();
        }
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(String dataEvento) {
        this.dataEvento = dataEvento;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public EstadoIngresso getEstado() {
        return estado;
    }

    public void setEstado(EstadoIngresso estado) {
        this.estado = estado;
    }

    /**
     * Gera um ID hash único para o ingresso
     * Combina cliente, evento, timestamp e UUID para garantir unicidade
     * 
     * @return hash SHA-256 do ingresso
     */
    public String gerarHashId() {
        try {
            // Criar dados únicos combinando diferentes informações
            String dadosUnicos = (nomeCliente != null ? nomeCliente : "cliente") +
                    (nomeEvento != null ? nomeEvento : "evento") +
                    System.currentTimeMillis() +
                    UUID.randomUUID().toString();

            // Criar instância de MessageDigest com SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dadosUnicos.getBytes());

            // Converter bytes para hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Fallback: usar UUID simples se SHA-256 não estiver disponível
            return UUID.randomUUID().toString();
        }
    }

    /**
     * Retorna o hash ID do ingresso
     * @return hash único do ingresso
     */
    public String getHashId() {
        // Se o hash não foi gerado ainda, gera agora
        if (this.hashId == null) {
            this.hashId = gerarHashId();
        }
        return this.hashId;
    }

    /**
     * Define manualmente o hash ID (usar com cuidado)
     * @param hashId novo hash ID
     */
    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    /**
     * Retorna uma representação curta do hash (primeiros 12 caracteres)
     * Útil para exibição em QR codes ou validação visual
     * @return hash curto
     */
    public String getHashIdCurto() {
        String hash = getHashId();
        return hash != null && hash.length() >= 12 ? hash.substring(0, 12).toUpperCase() : hash;
    }
}
