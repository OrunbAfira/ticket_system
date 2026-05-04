package com.mycompany.ingressos1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ingressos")
public abstract class Ingresso {

    @Id
    private String id;

    private String nomeEvento;
    private String nomeCliente;
    private String dataEvento;
    private double valorBase;
    private double valorFinal;
    private EstadoIngresso estado;

    public Ingresso() {
        this.estado = EstadoIngresso.RESERVADO;
    }

    public Ingresso(String nomeEvento, String nomeCliente, String dataEvento, double valorBase) {
        this.nomeEvento = nomeEvento;
        this.nomeCliente = nomeCliente;
        this.dataEvento = dataEvento;
        this.valorBase = valorBase;
        this.estado = EstadoIngresso.RESERVADO;
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

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
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
}