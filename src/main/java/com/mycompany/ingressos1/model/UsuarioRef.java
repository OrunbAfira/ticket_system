package com.mycompany.ingressos1.model;

/**
 * Referência leve de usuário para vincular documentos (MongoDB) sem embutir
 * a entidade completa.
 */
public class UsuarioRef {

    private String id;
    private String login;
    private String nome;

    public UsuarioRef() {
    }

    public UsuarioRef(String id, String login, String nome) {
        this.id = id;
        this.login = login;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
