# 🎫 Sistema de Ingressos para Eventos

[![GitHub](https://img.shields.io/badge/GitHub-OrunbAfira%2Fticket__system-blue?style=flat-square&logo=github)](https://github.com/OrunbAfira/ticket_system)
[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-green?style=flat-square)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-brightgreen?style=flat-square)](https://www.mongodb.com)

Plataforma web completa para gerenciamento de eventos e vendas de ingressos desenvolvida com **Java 17 + Spring Boot 3.3.5 + MongoDB**

## Conteúdo

- [Quick Start](#quick-start)
- [Funcionalidades](#funcionalidades)
- [Instalação](#instalação)
- [Uso](#uso)
- [Documentação](#documentação)

---

## Quick Start

```bash
# 1. Certifique-se que MongoDB está rodando
mongod

# 2. Compile e execute
mvn clean install
mvn spring-boot:run

# 3. Acesse
http://localhost:8080

# 4. Login
Usuário: admin
Senha: admin
```

---

## Funcionalidades

### Para Usuários
- Cadastro e login
- Buscar eventos
- Comprar ingressos (Normal, VIP, Meia)
- Pagamento integrado
- Imprimir ingressos
- Histórico de compras

### Para Administradores
- Criar e gerenciar eventos
- Visualizar vendas
- Gerenciar usuários
- Monitorar ingressos
- Controlar receita
---

## Instalação

### 1. Clonar o Projeto
```bash
git clone <url>
cd Sistema-de-Ingresso-main
```

### 2. Configurar MongoDB
```bash
# Iniciar MongoDB (em outro terminal)
mongod
```

### 3. Configurar application.properties
```properties
# MongoDB Atlas Configuration
spring.data.mongodb.uri=mongodb+srv://brunoassisfaria_db_user:dzVbOlx96CGd8mVW@cluster0.d2ugsqv.mongodb.net/ingressos_db?retryWrites=true&w=majority
spring.application.name=Ingressos1
server.port=8080
```

> **Nota:** As credenciais já estão configuradas no repositório

### 4. Executar
```bash
mvn clean install
mvn spring-boot:run
```

Acesse: **http://localhost:8080**

---

## Como Usar

### Login Padrão
```
Usuário: admin
Senha: admin
```

### Criar Novo Usuário
1. Clique em "Cadastro"
2. Preencha os dados
3. Clique "Registrar"

### Comprar Ingresso
1. Faça login
2. Acesse "Eventos"
3. Selecione um evento
4. Escolha tipo (Normal/VIP/Meia)
5. Confirme a compra
6. Efetue pagamento

### Criar Evento (Admin)
1. Login como admin
2. Acesse "Painel Admin"
3. Clique "Novo Evento"
4. Preencha dados (nome, data, local, valor)
5. Salve

---

## Documentação

- **[AUTHOR.md](AUTHOR.md)** - Informações de autoria
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - Arquitetura detalhada
- **[INSTALLATION.md](INSTALLATION.md)** - Guia completo
- **[CONTRIBUTING.md](CONTRIBUTING.md)** - Padrões de design
- **[VERSION.md](VERSION.md)** - Informações de versão
- **[WELCOME.txt](WELCOME.txt)** - Boas-vindas e guia rápido

---