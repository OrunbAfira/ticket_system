/*
 * Sistema de Ingressos para Eventos
 * Classe Modelo: Evento
 */
package com.mycompany.ingressos1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe Evento
 * Representa um evento no sistema com todas as suas características
 * Segue os princípios de Orientação a Objeto
 * 
 * @author Sistema de Ingressos
 */
@Document(collection = "eventos")
public class Evento {

    @Id
    private String id;

    // Atributos de Identificação e Descrição
    private String nome;
    private String descricao;

    // Atributos de Data e Horário
    private String data; // Formato: YYYY-MM-DD
    private String horario; // Formato: HH:mm

    // Atributo de Localização
    private String local;

    // Atributos de Ingressos
    private int quantidadeIngressosDisponiveis;
    private int ingressosVendidos;
    private double valorIngresso;

    /**
     * Construtor padrão (necessário para MongoDB)
     */
    public Evento() {
        this.ingressosVendidos = 0;
    }

    /**
     * Construtor completo com todos os atributos
     */
    public Evento(String nome, String descricao, String data, String horario, 
                  String local, int quantidadeIngressos, double valorIngresso) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.horario = horario;
        this.local = local;
        this.quantidadeIngressosDisponiveis = quantidadeIngressos;
        this.valorIngresso = valorIngresso;
        this.ingressosVendidos = 0;
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getQuantidadeIngressosDisponiveis() {
        return quantidadeIngressosDisponiveis;
    }

    public void setQuantidadeIngressosDisponiveis(int quantidade) {
        this.quantidadeIngressosDisponiveis = quantidade;
    }

    public int getIngressosVendidos() {
        return ingressosVendidos;
    }

    public void setIngressosVendidos(int vendidos) {
        this.ingressosVendidos = vendidos;
    }

    public double getValorIngresso() {
        return valorIngresso;
    }

    public void setValorIngresso(double valor) {
        this.valorIngresso = valor;
    }

    // Propriedade derivada: valorBase (para compatibilidade)
    public double getValorBase() {
        return this.valorIngresso;
    }

    public void setValorBase(double valor) {
        this.valorIngresso = valor;
    }

    // Métodos de Negócio (Business Logic)

    /**
     * Verifica se há ingressos disponíveis
     * @return true se há ingressos disponíveis, false caso contrário
     */
    public boolean temIngressosDisponiveis() {
        return (quantidadeIngressosDisponiveis - ingressosVendidos) > 0;
    }

    /**
     * Retorna a quantidade de ingressos ainda não vendidos
     * @return número de ingressos disponíveis
     */
    public int getIngressosRestantes() {
        return quantidadeIngressosDisponiveis - ingressosVendidos;
    }

    /**
     * Verifica se ainda há ingressos disponíveis para a quantidade solicitada
     * @param quantidade quantidade desejada de ingressos
     * @return true se há quantidade suficiente, false caso contrário
     */
    public boolean temIngressosSuficientes(int quantidade) {
        return getIngressosRestantes() >= quantidade;
    }

    /**
     * Realiza a venda de ingressos (incrementa o contador)
     * @param quantidade quantidade de ingressos a vender
     * @return true se a venda foi bem-sucedida, false caso contrário
     */
    public boolean venderIngressos(int quantidade) {
        if (temIngressosSuficientes(quantidade)) {
            this.ingressosVendidos += quantidade;
            return true;
        }
        return false;
    }

    /**
     * Calcula o valor total de venda para uma quantidade de ingressos
     * @param quantidade quantidade de ingressos
     * @return valor total
     */
    public double calcularValorTotal(int quantidade) {
        return this.valorIngresso * quantidade;
    }

    /**
     * Retorna a data e hora formatadas
     * @return String com data e hora
     */
    public String getDataHoraFormatada() {
        if (data != null && horario != null) {
            return data + " às " + horario;
        }
        return data != null ? data : "Data não definida";
    }

    /**
     * Retorna o percentual de ingressos vendidos
     * @return percentual (0 a 100)
     */
    public double getPercentualVenda() {
        if (quantidadeIngressosDisponiveis == 0) {
            return 0;
        }
        return (double) ingressosVendidos / quantidadeIngressosDisponiveis * 100;
    }

    /**
     * Verifica se o evento está lotado
     * @return true se todos os ingressos foram vendidos
     */
    public boolean estaLotado() {
        return ingressosVendidos >= quantidadeIngressosDisponiveis;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", data='" + data + '\'' +
                ", horario='" + horario + '\'' +
                ", local='" + local + '\'' +
                ", ingressos disponíveis=" + getIngressosRestantes() +
                ", valor=" + valorIngresso +
                '}';
    }
}