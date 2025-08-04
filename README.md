# 🍽️ Chefia - Sistema de Gestão para Restaurantes

## 📌 Sobre o Projeto

O sistema de gestão para restaurantes é uma aplicação web desenvolvida para atender coletivamente pequenos e médios estabelecimentos gastronômicos da região. O aplicativo permite o gerenciamento de usuários e endereços. O objetivo central é proporcionar uma solução acessível, escalável e segura, que substitua sistemas caros e isolados por uma plataforma compartilhada, moderna e fácil de usar.

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


## 🧱 Estrutura do Projeto

📦 chefia
├── 📁 src
│ └── 📁 main
│   ├── 📁 java/com/chefia
│     ├── 📁 controllers
│     ├── 📁 dtos
│     ├── 📁 entities
│     ├── 📁 exceptions
│     ├── 📁 infra
│     ├── 📁 mapper
│     ├── 📁 repositories
│     ├── 📁 services
│     ├── 📁 validation
│ └── 📁 resources
│   ├── 📁 api
|     ├── 📄 addresses.yaml
|     ├── 📄 users.yaml
│   ├── 📁 db
│     ├── 📁 migration
|   ├── 📄 application.yaml
|   ├── 📄 application-local.yaml
│ 
├── 📄 Dockerfile
├── 📄 docker-compose.yml
├── 📄 README.md
├── 📄 pom.xml

## 🐳 Como Rodar com Docker Compose

### Pré-requisitos:

- Docker

### Comando:

```bash
docker-compose up --build
```

O sistema estará disponível em: http://localhost:8080

## 🔐 Autenticação
O sistema utiliza autenticação baseada em JWT. Após o login, será fornecido um token que deverá ser enviado no header Authorization nas próximas requisições protegidas:

Authorization: Bearer <token>

## 📚 Documentação da API
Acesse a documentação interativa via Swagger:

http://localhost:8080/swagger-ui.html
