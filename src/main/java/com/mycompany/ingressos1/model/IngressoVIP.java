/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ingressos1.model;

/**
 *
 * @author RafaelG
 */
public class IngressoVIP extends Ingresso {

    public IngressoVIP() {
        super();
    }

    public IngressoVIP(String nomeEvento, String nomeCliente, String dataEvento, double valorBase) {
        super(nomeEvento, nomeCliente, dataEvento, valorBase);
    }

    @Override
    public double calcularValor() {
        return getValorBase() * 1.5;
    }

    @Override
    public String imprimirIngresso() {
        return "Ingresso VIP - Evento: " + getNomeEvento()
                + " - Cliente: " + getNomeCliente()
                + " - Valor: R$ " + calcularValor();
    }

    @Override
    public String getTipoIngresso() {
        return "VIP";
    }
}
    

