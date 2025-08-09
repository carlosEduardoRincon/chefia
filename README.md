# 🍽️ Chefia - Sistema de Gestão para Restaurantes

O Chefia é uma microsserviço backend desenvolvido para atender a gestão de restaurantes. A aplicação permite o gerenciamento de usuários e seus respectivos endereços. O objetivo central é proporcionar uma solução acessível, escalável e segura.

## 🚀 Tecnologias Utilizadas

- ✅ Java 17
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

**Documentação da API:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- Para visualizar a aba de endereços buscar por '/api/addresses.yaml'

## 📂 Documentação Adicional
No diretório /docs, você encontrará:
- Arquivos de referência técnica do projeto
- A coleção do Postman contendo os endpoints disponíveis, já prontos para teste
