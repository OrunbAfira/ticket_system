package com.mycompany.ingressos1;

public class AppConfig {
    
    // Informações da Aplicação
    public static final String APP_NAME = "Sistema de Ingressos para Eventos";
    public static final String APP_VERSION = "1.0.0";
    public static final String DEVELOPER = "Bruno";
    
    // Constantes de Segurança
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int SESSION_TIMEOUT_MINUTES = 30;
    
    // Constantes de Ingressos
    public static final double INGRESSO_VIP_MULTIPLIER = 1.5;
    public static final double INGRESSO_MEIA_MULTIPLIER = 0.5;
    public static final double INGRESSO_NORMAL_MULTIPLIER = 1.0;
    
    // Constantes de Perfis
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    
    // Estados de Ingresso
    public static final String STATE_RESERVADO = "RESERVADO";
    public static final String STATE_PAGO = "PAGO";
    public static final String STATE_UTILIZADO = "UTILIZADO";
    public static final String STATE_CANCELADO = "CANCELADO";
    
    // Mensagens
    public static final String MSG_LOGIN_SUCESSO = "Login realizado com sucesso!";
    public static final String MSG_LOGIN_ERRO = "Usuário ou senha inválidos!";
    public static final String MSG_EVENTO_CRIADO = "Evento criado com sucesso!";
    public static final String MSG_INGRESSO_COMPRADO = "Ingresso comprado com sucesso!";
    
    // Limites
    public static final int MAX_EVENTOS_POR_PAGINA = 10;
    public static final int MAX_INGRESSOS_POR_COMPRA = 10;
    
    private AppConfig() {
        // Prevent instantiation
    }
}
