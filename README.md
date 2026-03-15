# Api_NotificacaoUCM

> **Task & Lecturer Presence Notification System** — A Java/Spring Boot REST API built for the Universidade Católica de Moçambique (UCM), delivering real-time notifications about academic tasks and lecturer attendance to students and staff.

---

## Table of Contents

- [Overview](#overview)
- [How It Works](#how-it-works)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Running with the Maven Wrapper](#running-with-the-maven-wrapper)
- [Environment Variables](#environment-variables)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Roadmap](#roadmap)
- [Author](#author)

---

## Overview

**Api_NotificacaoUCM** is a backend notification service developed for UCM (Universidade Católica de Moçambique). It solves a real institutional problem: keeping students informed about upcoming academic tasks (assignments, exams, deadlines) and lecturer attendance status in real time.

The system enables:

- **Task notifications** — alert students about new assignments, deadlines, and exam schedules
- **Lecturer presence tracking** — notify students when a lecturer confirms or cancels their presence for a class
- **Push/in-app notification dispatch** — deliver notifications to mobile and web clients
- **Role-aware messaging** — different notification flows for students, lecturers, and administrators

---

## How It Works

```
Lecturer / Admin
      │
      ▼
  REST API (Spring Boot)
      │
      ├─── Creates task or updates presence status
      │
      ▼
  Notification Service
      │
      ├─── Persists notification to DB
      │
      ▼
  Push Dispatcher (Firebase FCM / WebSocket)
      │
      ▼
  Student Devices (Mobile / Web)
```

When a lecturer registers their presence or a new task is created, the API triggers a notification event that is dispatched to all relevant subscribers (students enrolled in that course/class).

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot |
| Build Tool | Maven (Maven Wrapper included) |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL / MySQL |
| Push Notifications | Firebase Cloud Messaging (FCM) |
| Security | Spring Security + JWT |
| Architecture | Layered (Controller → Service → Repository) |
| Testing | JUnit 5 + Mockito |

> **Note:** The presence of `package-lock.json` in the root suggests a Node.js utility layer or Firebase Admin SDK integration alongside the Java backend.

---

## Project Structure

```
Api_NotificacaoUCM/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ucm/notificacao/
│   │   │       ├── controller/        # REST endpoints
│   │   │       ├── service/           # Business and notification logic
│   │   │       ├── repository/        # JPA data access
│   │   │       ├── model/             # Domain entities (Task, Presence, Notification, User)
│   │   │       ├── dto/               # Request/Response DTOs
│   │   │       ├── security/          # JWT auth filters
│   │   │       └── config/            # Firebase, beans, CORS config
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/                      # Unit and integration tests
├── .idea/.mvn/wrapper/                # Maven wrapper (IntelliJ project)
├── pom.xml                            # Maven dependencies
├── mvnw / mvnw.cmd                    # Maven wrapper scripts
├── package-lock.json                  # Node.js / Firebase Admin dependencies
└── .gitignore
```

---

## Getting Started

### Prerequisites

- [Java 17+](https://adoptium.net/)
- A running PostgreSQL or MySQL database
- Firebase project with FCM enabled *(for push notifications)*
- [Git](https://git-scm.com/)

### Running with the Maven Wrapper

```bash
# Clone the repository
git clone https://github.com/ManuelMassora/Api_NotificacaoUCM.git
cd Api_NotificacaoUCM

# Configure environment variables (see section below)

# Unix / macOS / Linux
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The API will be available at `http://localhost:8080`.

To build a production JAR:

```bash
./mvnw clean package -DskipTests
java -jar target/api-notificacao-ucm-*.jar
```

---

## Environment Variables

Configure `src/main/resources/application.properties`:

```properties
# Server
server.port=8080
spring.profiles.active=dev

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/ucm_notificacao
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# Firebase FCM
firebase.credentials.path=classpath:firebase-service-account.json
firebase.project.id=your_firebase_project_id
```

> Place your Firebase service account JSON file in `src/main/resources/` and add it to `.gitignore`. Never commit credentials to version control.

---

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/auth/login` | Authenticate and receive JWT token |
| `POST` | `/api/v1/auth/register` | Register a new user |

### Tasks
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/tasks` | List all tasks |
| `POST` | `/api/v1/tasks` | Create a new task (triggers notification) |
| `GET` | `/api/v1/tasks/{id}` | Get task details |
| `PUT` | `/api/v1/tasks/{id}` | Update a task |
| `DELETE` | `/api/v1/tasks/{id}` | Delete a task |

### Lecturer Presence
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/presence` | Register lecturer presence/absence (triggers notification) |
| `GET` | `/api/v1/presence` | List presence records |
| `GET` | `/api/v1/presence/{lecturerId}` | Get presence history for a lecturer |

### Notifications
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/notifications` | Get notifications for the current user |
| `PUT` | `/api/v1/notifications/{id}/read` | Mark a notification as read |
| `DELETE` | `/api/v1/notifications/{id}` | Delete a notification |

### Device Tokens (FCM)
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/devices/register` | Register a device token for push notifications |
| `DELETE` | `/api/v1/devices/{token}` | Unregister a device token |

---

## Running Tests

```bash
# Run all tests
./mvnw test

# Run with detailed output
./mvnw test -Dsurefire.useFile=false

# Run a specific test class
./mvnw test -Dtest=NotificationServiceTest
```

---

## Roadmap

- [ ] Complete FCM push notification integration
- [ ] Lecturer presence real-time status (WebSocket)
- [ ] Notification history and read/unread state
- [ ] Student subscription by course/class
- [ ] Email notification channel
- [ ] Admin dashboard for broadcast notifications
- [ ] Swagger/OpenAPI documentation
- [ ] Docker containerization
- [ ] CI/CD pipeline

---

## Author

**Manuel Massora** — Backend Engineer  
Maputo, Mozambique

- GitHub: [@ManuelMassora](https://github.com/ManuelMassora)
- LinkedIn: [manuelt-massora-5bb417375](https://linkedin.com/in/manuelt-massora-5bb417375/)
- Email: manuelmassora75@gmail.com

---

> Built for UCM · Java · Spring Boot · Firebase FCM


#####################################


# Api_NotificacaoUCM

> **Sistema de Notificação de Tarefas e Presença de Docente** — API REST em Java/Spring Boot desenvolvida para a Universidade Católica de Moçambique (UCM), entregando notificações em tempo real sobre tarefas académicas e presença de docentes a estudantes e funcionários.

---

## Índice

- [Visão Geral](#visão-geral)
- [Como Funciona](#como-funciona)
- [Stack Tecnológica](#stack-tecnológica)
- [Estrutura do Projecto](#estrutura-do-projecto)
- [Como Começar](#como-começar)
  - [Pré-requisitos](#pré-requisitos)
  - [Executar com o Maven Wrapper](#executar-com-o-maven-wrapper)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Endpoints da API](#endpoints-da-api)
- [Executar Testes](#executar-testes)
- [Roadmap](#roadmap)
- [Autor](#autor)

---

## Visão Geral

**Api_NotificacaoUCM** é um serviço backend de notificações desenvolvido para a UCM (Universidade Católica de Moçambique). Resolve um problema institucional real: manter os estudantes informados sobre tarefas académicas (trabalhos, exames, prazos) e o estado de presença dos docentes em tempo real.

O sistema permite:

- **Notificações de tarefas** — alertar estudantes sobre novos trabalhos, prazos e calendário de exames
- **Rastreio de presença de docente** — notificar estudantes quando um docente confirma ou cancela a sua presença numa aula
- **Envio de notificações push/in-app** — entregar notificações a clientes móveis e web
- **Mensagens por função** — fluxos de notificação distintos para estudantes, docentes e administradores

---

## Como Funciona

```
Docente / Administrador
         │
         ▼
   API REST (Spring Boot)
         │
         ├─── Cria tarefa ou actualiza estado de presença
         │
         ▼
   Serviço de Notificações
         │
         ├─── Persiste notificação na base de dados
         │
         ▼
   Dispatcher Push (Firebase FCM / WebSocket)
         │
         ▼
   Dispositivos dos Estudantes (Mobile / Web)
```

Quando um docente regista a sua presença ou uma nova tarefa é criada, a API dispara um evento de notificação que é enviado a todos os subscritores relevantes (estudantes inscritos nessa cadeira/turma).

---

## Stack Tecnológica

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17+ |
| Framework | Spring Boot |
| Build Tool | Maven (Maven Wrapper incluído) |
| ORM | Spring Data JPA / Hibernate |
| Base de Dados | PostgreSQL / MySQL |
| Notificações Push | Firebase Cloud Messaging (FCM) |
| Segurança | Spring Security + JWT |
| Arquitectura | Camadas (Controller → Service → Repository) |
| Testes | JUnit 5 + Mockito |

> **Nota:** A presença de `package-lock.json` na raiz indica uma camada utilitária Node.js ou integração com o Firebase Admin SDK a par do backend Java.

---

## Estrutura do Projecto

```
Api_NotificacaoUCM/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ucm/notificacao/
│   │   │       ├── controller/        # Endpoints REST
│   │   │       ├── service/           # Lógica de negócio e de notificação
│   │   │       ├── repository/        # Acesso a dados JPA
│   │   │       ├── model/             # Entidades (Tarefa, Presença, Notificação, Utilizador)
│   │   │       ├── dto/               # DTOs de pedido/resposta
│   │   │       ├── security/          # Filtros de autenticação JWT
│   │   │       └── config/            # Firebase, beans, configuração CORS
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/                      # Testes unitários e de integração
├── .idea/.mvn/wrapper/                # Maven wrapper (projecto IntelliJ)
├── pom.xml                            # Dependências Maven
├── mvnw / mvnw.cmd                    # Scripts Maven Wrapper
├── package-lock.json                  # Dependências Node.js / Firebase Admin
└── .gitignore
```

---

## Como Começar

### Pré-requisitos

- [Java 17+](https://adoptium.net/)
- Uma instância a correr de PostgreSQL ou MySQL
- Projecto Firebase com FCM activado *(para notificações push)*
- [Git](https://git-scm.com/)

### Executar com o Maven Wrapper

```bash
# Clonar o repositório
git clone https://github.com/ManuelMassora/Api_NotificacaoUCM.git
cd Api_NotificacaoUCM

# Configurar variáveis de ambiente (ver secção abaixo)

# Unix / macOS / Linux
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

Para gerar um JAR de produção:

```bash
./mvnw clean package -DskipTests
java -jar target/api-notificacao-ucm-*.jar
```

---

## Variáveis de Ambiente

Configura em `src/main/resources/application.properties`:

```properties
# Servidor
server.port=8080
spring.profiles.active=dev

# Base de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/ucm_notificacao
spring.datasource.username=o_teu_utilizador
spring.datasource.password=a_tua_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
jwt.secret=a_tua_chave_secreta_jwt
jwt.expiration=86400000

# Firebase FCM
firebase.credentials.path=classpath:firebase-service-account.json
firebase.project.id=o_teu_firebase_project_id
```

> Coloca o ficheiro JSON da conta de serviço Firebase em `src/main/resources/` e adiciona-o ao `.gitignore`. Nunca faças commit de credenciais no controlo de versão.

---

## Endpoints da API

### Autenticação
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/auth/login` | Autenticar e receber token JWT |
| `POST` | `/api/v1/auth/register` | Registar um novo utilizador |

### Tarefas
| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/v1/tasks` | Listar todas as tarefas |
| `POST` | `/api/v1/tasks` | Criar nova tarefa (dispara notificação) |
| `GET` | `/api/v1/tasks/{id}` | Obter detalhes de uma tarefa |
| `PUT` | `/api/v1/tasks/{id}` | Actualizar uma tarefa |
| `DELETE` | `/api/v1/tasks/{id}` | Eliminar uma tarefa |

### Presença de Docente
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/presence` | Registar presença/ausência do docente (dispara notificação) |
| `GET` | `/api/v1/presence` | Listar registos de presença |
| `GET` | `/api/v1/presence/{lecturerId}` | Histórico de presença de um docente |

### Notificações
| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/v1/notifications` | Obter notificações do utilizador actual |
| `PUT` | `/api/v1/notifications/{id}/read` | Marcar notificação como lida |
| `DELETE` | `/api/v1/notifications/{id}` | Eliminar uma notificação |

### Tokens de Dispositivo (FCM)
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/devices/register` | Registar token de dispositivo para push |
| `DELETE` | `/api/v1/devices/{token}` | Remover registo de dispositivo |

---

## Executar Testes

```bash
# Executar todos os testes
./mvnw test

# Executar com saída detalhada
./mvnw test -Dsurefire.useFile=false

# Executar uma classe de teste específica
./mvnw test -Dtest=NotificationServiceTest
```

---

## Roadmap

- [ ] Integração completa FCM para notificações push
- [ ] Estado de presença do docente em tempo real (WebSocket)
- [ ] Histórico de notificações e estado lido/não lido
- [ ] Subscrição de estudante por cadeira/turma
- [ ] Canal de notificação por email
- [ ] Painel de administração para notificações em massa
- [ ] Documentação Swagger/OpenAPI
- [ ] Contenorização com Docker
- [ ] Pipeline CI/CD

---

## Autor

**Manuel Massora** — Backend Engineer  
Maputo, Moçambique

- GitHub: [@ManuelMassora](https://github.com/ManuelMassora)
- LinkedIn: [manuelt-massora-5bb417375](https://linkedin.com/in/manuelt-massora-5bb417375/)
- Email: manuelmassora75@gmail.com

---

> Desenvolvido para a UCM · Java · Spring Boot · Firebase FCM
