# Serasa Person API

API REST em Spring Boot para gerenciamento de pessoas, construído como parte do desafio da Serasa Experian.

---

## 🔧 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Validation (Bean Validation)
- JUnit 5 (JUnit Jupiter)
- Mockito
- H2 (banco de dados em memória)
- Swagger OpenAPI

---

## 🔐 Autenticação e Autorização

A API utiliza autenticação via JWT e controle de acesso por perfil:

- `ADMIN`: pode criar, atualizar, remover e consultar pessoas
- `USER`: pode apenas consultar pessoas

### Endpoint de login

```
POST /auth/login
```

**Body:**
```json
{
  "username": "admin",
  "password": "admin"
}
```

**Resposta:**
```json
{
  "token": "Bearer eyJhbGciOiJIUzI1..."
}
```
**admin/admin autenticará com permissões de administrador**

---

## 👤 Endpoints de Pessoa

| Método | Endpoint             | Descrição                     | Perfil |
|--------|----------------------|-------------------------------|--------|
| GET    | `/persons`           | Buscar pessoas com filtros    | Todos  |
| POST   | `/persons`           | Criar nova pessoa             | ADMIN  |
| PATCH  | `/persons/{id}`      | Atualizar dados da pessoa     | ADMIN  |
| DELETE | `/persons/{id}`      | Remover pessoa por ID         | ADMIN  |

---

## 📄 Validações

### Pessoa

- `@NotBlank`: nome, telefone e cep obrigatórios (em criação)
- `@ValidPhone`: telefone no formato brasileiro com ou sem nono dígito
- `@NotBlankIfNotNull`: campos que devem ser preenchidos se fornecidos (em atualização)
- `@Min` e `@Max`: limites para idade e score
- `score`: deve estar entre 0 e 1000

### Enum de Descrição do Score

O campo `score_description` é atribuído com base no score usando o enum `ScoreDescription`:

| Score       | Descrição     |
|-------------|---------------|
| 0–200       | Insuficiente  |
| 201–500     | Inaceitável   |
| 501–700     | Aceitável     |
| 701–1000    | Recomendável  |

---

## 📑 Documentação da API

Disponível via Swagger UI em:

```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Executando os Testes

Para rodar todos os testes automatizados:

```
mvn test
```


---

## ▶️ Executando a Aplicação

1. Clone o projeto:
```bash
git clone https://github.com/seu-usuario/serasa-person-api.git
cd serasa-person-api
```

2. Execute com:
```bash
mvn spring-boot:run
```

3. Acesse em:
```
http://localhost:8080
```

---

## 🗂 Estrutura de Pacotes

```
com.serasa.personapi
├── domain                # Regras de negócio de domínio
├── infrastructure
│   ├── config            # Configurações customizadas
│   ├── client            # Interfaces com outros serviços
│   └── exchange          # DTOs de requisição e resposta
│   └── etc...
└── web                   # Rest Controllers
```

---

## ✅ Pré-requisitos

- Java 17 ou superior
- Maven 3.8+

---
