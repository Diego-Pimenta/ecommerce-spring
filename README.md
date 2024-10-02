# Backend Ecommerce Spring

## 📜 Descrição

O projeto é uma aplicação de ecommerce capaz de criar e gerenciar produtos, estoque dos produtos, vendas e usuários.

## 🚀 Tecnologias

- Java
- Maven
- Docker
- Spring Boot / JPA / Validation / Security / Mail / Hateoas
- Redis
- MySQL / H2
- Flyway
- SpringDoc OpenAPI
- MapStruct
- JJWT
- Mockito
- TestContainers

## 🏛️ Arquitetura

<img src="/docs/Class_diagram.png" alt="Diagrama de Classes">

## 📦 Instruções de Uso

1. Clone o repositório com o Git.
```bash
git clone https://github.com/Diego-Pimenta/ecommerce-spring.git
```

2. Entre na pasta raiz do projeto e inicie os containers definidos no "docker-compose.yaml".
```bash
cd ecommerce-spring
docker compose up -d
```

3. Interaja com a API no Postman ou Insomnia com a seguinte URL:
```bash
http://localhost:8080/api
```

4. Para acessar a plataforma com contas já cadastradas no banco de dados, utilize os seguintes dados de login:

- Admin - CPF: 12345678901 | Senha: adminpass.
- Client - CPF: 12345678902 | Senha: password1.
- Client - CPF: 12345678903 | Senha: password2.

## ⚠️ Avisos

Preste atenção que para o funcionamento adequado da aplicação é importante que **variáveis de ambiente** propriamente definidas sejam utilizadas.
Logo, tome cautela, configure a aplicação de acordo com suas propriedades. Para alterar diretamente na aplicação e não no Docker, se atente
para as propriedades do **banco de dados, redis, jwt e serviço de email.**
