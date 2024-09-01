# Backend Ecommerce Spring

## Descrição

O projeto é uma aplicação de ecommerce capaz de criar e gerenciar produtos, estoque dos produtos, vendas e usuários.

## Requisitos

- Certifique-se de ter o Docker instalado para a execução da aplicação.

## Instruções de Uso

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

- Admin - CPF: 12345678901, Senha: adminpass.
- Client - CPF: 12345678902, Senha: password1.
- Client - CPF: 12345678903, Senha: password2.

## Diagrama de Classes

<img src="/docs/Class_diagram.png" alt="Diagrama de Classes">
