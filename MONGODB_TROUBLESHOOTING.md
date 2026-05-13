# 🛠️ Guia de Resolução - Erro de Autenticação MongoDB

## Problema
Ao tentar registrar um novo usuário ou fazer login, você recebe o erro:
```
Exception authenticating MongoCredential{mechanism=SCRAM-SHA-1, ...}
```

## Causas Possíveis

1. **Credenciais Incorretas** - A senha pode ter sido alterada ou expirada
2. **Usuário Não Existe** - O usuário foi removido do MongoDB Atlas
3. **Cluster Offline** - O cluster MongoDB Atlas pode estar pausado ou desconectado
4. **Problemas de Conectividade** - Firewall ou limitações de rede

## Soluções

### Opção 1: Corrigir Credenciais do MongoDB Atlas (Recomendado)

1. Acesse [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Faça login com sua conta
3. Navegue até **Database Access** (Acesso ao Banco de Dados)
4. Localize o usuário `brunoassisfaria_db_user`
5. Se não existir, crie um novo usuário:
   - Username: `brunoassisfaria_db_user` (ou outro de sua escolha)
   - Password: Gere uma senha segura
   - Role: `readWrite`
6. Copie a senha e atualize em `src/main/resources/application.properties`:
   ```properties
   spring.data.mongodb.uri=mongodb+srv://SEU_USUARIO:SUA_SENHA@cluster0.d2ugsqv.mongodb.net/ingressos_db?retryWrites=true&w=majority&authSource=admin
   ```
7. Recompile: `mvn clean compile`
8. Execute novamente: `mvn spring-boot:run`

### Opção 2: Usar MongoDB Local (Para Desenvolvimento)

#### Pré-requisitos
- Docker instalado ([Download](https://www.docker.com/products/docker-desktop))

#### Passo a Passo

1. Inicie MongoDB em Docker:
   ```bash
   docker run -d -p 27017:27017 \
     -e MONGO_INITDB_ROOT_USERNAME=admin \
     -e MONGO_INITDB_ROOT_PASSWORD=admin123 \
     --name mongodb_local \
     mongo:latest
   ```

2. Aguarde alguns segundos para o MongoDB iniciar

3. Atualize `src/main/resources/application.properties`:
   ```properties
   spring.data.mongodb.uri=mongodb://admin:admin123@localhost:27017/ingressos_db?authSource=admin
   ```

4. Recompile e execute:
   ```bash
   mvn clean compile
   mvn spring-boot:run
   ```

5. Teste com as credenciais:
   - Usuário: `admin`
   - Senha: `admin`

### Opção 3: Instalar MongoDB Localmente

#### No Windows

1. Baixe [MongoDB Community Server](https://www.mongodb.com/try/download/community)
2. Execute o instalador
3. Escolha "Install MongoDB as a Service"
4. Complete a instalação
5. MongoDB iniciará automaticamente na porta 27017

#### Configurar o Banco

Abra Mongosh (MongoDB Shell) e execute:
```javascript
use admin
db.createUser({
  user: "ingressos_user",
  pwd: "ingressos123",
  roles: [{ role: "root", db: "admin" }]
})

use ingressos_db
db.createCollection("usuario")
db.createCollection("evento")
db.createCollection("ingresso")
```

#### Atualizar application.properties

```properties
spring.data.mongodb.uri=mongodb://ingressos_user:ingressos123@localhost:27017/ingressos_db?authSource=admin
```

## Testando a Conexão

Após configurar, verifique se a conexão funciona:

1. Recompile:
   ```bash
   mvn clean compile -DskipTests
   ```

2. Execute:
   ```bash
   mvn spring-boot:run
   ```

3. Acesse: http://localhost:8080

4. Tente fazer login com:
   - Usuário: `admin`
   - Senha: `admin`

5. Se entrar com sucesso, a conexão está funcionando!

6. Teste registrar um novo usuário

## Verificar Status do Cluster MongoDB Atlas

1. Acesse [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Na tela inicial, procure pelo cluster `cluster0`
3. Verifique se o status é "Running" (em execução)
4. Se estiver "Paused", clique em "Resume"
5. Verifique as limitações de rede (IP Whitelist)

## Precisa de Ajuda?

- Verifique os logs da aplicação em `mvn spring-boot:run`
- Procure por mensagens de erro relacionadas a MongoDB
- Verifique se a porta 27017 está aberta (se usando local)
- Teste a conexão usando MongoDB Compass (ferramenta gráfica)

---

**Dica:** Se o MongoDB Atlas está em camada gratuita, o cluster pode dormir após alguns minutos de inatividade. Aguarde 1-2 minutos e tente novamente.
