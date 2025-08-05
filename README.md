# 🍽️ Chefia - Sistema de Gestão para Restaurantes

O Chefia é uma aplicação web desenvolvida para atender restaurante da região. A aplicação permite o gerenciamento de usuários e seus respectivos endereços. O objetivo central é proporcionar uma solução acessível, escalável e segura, que substitua sistemas caros e isolados por uma plataforma compartilhada, moderna e fácil de usar.

## 🚀 Tecnologias Utilizadas

- ✅ Java 17+
- ✅ Spring Boot
- ✅ Spring Security
- ✅ JPA 
- ✅ PostgreSQL
- ✅ Docker & Docker Compose
- ✅ JUnit & Mockito
- ✅ Lombok
- ✅ OpenAPI / Swagger

## 🐳 Como Rodar com Docker Compose

### Pré-requisitos:

- Docker

### Comando:

```bash
docker-compose up --build
```

O sistema estará disponível em: http://localhost:8080

## 🔐 Autenticação com JWT
Este sistema utiliza autenticação baseada em JWT (JSON Web Token) para proteger seus endpoints. Após realizar login, será gerado um token que deverá ser enviado no cabeçalho das requisições autenticadas:

Authorization: Bearer <seu_token_jwt>
- Garanta que o token seja incluído corretamente em todas as requisições às rotas protegidas.

## 📚 Documentação da API
  - Swagger UI - A documentação interativa da API pode ser acessada diretamente no navegador:
  
  **Documentação da API:** http://localhost:8080/swagger

## 📂 Documentação Adicional
  No diretório /docs, você encontrará:
  - Arquivos de referência técnica do projeto
  - A coleção do Postman contendo os endpoints disponíveis, já prontos para teste

