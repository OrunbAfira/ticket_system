# Informações de Autoria

## Desenvolvedor Principal

**Nome:** Bruno (OrunbAfira)  
**Versão:** 1.0.0  
**Período:** 2024 - Maio de 2026  
**GitHub:** [@OrunbAfira](https://github.com/OrunbAfira)  
**Repositório:** [ticket_system](https://github.com/OrunbAfira/ticket_system)  

---

## Projeto

**Nome:** Sistema de Ingressos para Eventos  
**Tipo:** Aplicação Web Full Stack  
**Linguagem:** Java 17  
**Framework:** Spring Boot 3.3.5  
**Banco:** MongoDB  

---

## Componentes

### Controllers
- LoginController - Autenticação
- EventoController - Gerenciamento de eventos
- IngressoController - Gerenciamento de ingressos

### Models
- Usuario, Evento, Ingresso (abstract)
- IngressoNormal, IngressoVIP, IngressoMeia
- PerfilUsuario, EstadoIngresso

### Services
- UsuarioService
- EventoService
- IngressoService

### Repositories
- UsuarioRepository
- EventoRepository
- IngressoRepository

---

## Funcionalidades

- Autenticação com perfis (ADMIN, USER)
- CRUD completo de eventos
- Sistema de venda de ingressos (3 categorias)
- Cálculo automático de preços
- Gerenciamento de estados
- Geração de ingressos para impressão
- Dashboard administrativo
- Interface web com Thymeleaf
- Persistência em MongoDB

---

## Padrões de Design

- MVC (Model-View-Controller)
- Template Method
- Dependency Injection
- Repository Pattern
- Service Layer Pattern

---

## Login Padrão

Usuário: admin  
Senha: admin  

---

## Como Executar

```bash
git clone <url>
cd Sistema-de-Ingresso-main
mvn clean install
mvn spring-boot:run
```

Acesse: http://localhost:8080


