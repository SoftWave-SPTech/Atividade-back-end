# Sistema de Gerenciamento de Chamados - API REST

API REST desenvolvida com Spring Boot para gerenciamento de chamados utilizando estruturas de dados (Fila, Pilha e Lista).

## 📋 Funcionalidades

O sistema implementa três estruturas de dados fundamentais:

- **Fila (Queue - FIFO)**: Chamados comuns são atendidos na ordem de chegada (First In, First Out)
- **Pilha (Stack - LIFO)**: Chamados de emergência são empilhados para resolução imediata (Last In, First Out)
- **Lista (List)**: Histórico completo de todos os chamados criados no sistema

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.2.0
- Maven
- Lombok

## 📦 Instalação e Execução

### Pré-requisitos
- JDK 17 ou superior
- Maven 3.6 ou superior

### Passos para executar

1. Clone o repositório:
```bash
git clone https://github.com/SoftWave-SPTech/Atividade-back-end.git
cd Atividade-back-end
```

2. Compile o projeto:
```bash
mvn clean install
```

3. Execute a aplicação:
```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

## 📚 Endpoints da API

### 1. Criar Novo Chamado
**POST** `/chamados`

Cria um novo chamado que será adicionado à fila (se comum) ou pilha (se emergência).

**Request Body:**
```json
{
  "descricao": "Descrição do problema",
  "tipo": "COMUM"
}
```

Tipos disponíveis: `COMUM` ou `EMERGENCIA`

**Response:** (201 Created)
```json
{
  "id": 1,
  "descricao": "Descrição do problema",
  "tipo": "COMUM",
  "dataHoraCriacao": "2025-11-05T00:17:58.137177516",
  "dataHoraAtendimento": null,
  "status": "AGUARDANDO"
}
```

### 2. Listar Chamados na Fila (FIFO)
**GET** `/chamados/fila`

Lista todos os chamados comuns aguardando atendimento na ordem de chegada.

**Response:** (200 OK)
```json
[
  {
    "id": 1,
    "descricao": "Problema no computador",
    "tipo": "COMUM",
    "dataHoraCriacao": "2025-11-05T00:17:58.137177516",
    "dataHoraAtendimento": null,
    "status": "AGUARDANDO"
  }
]
```

### 3. Listar Chamados na Pilha (LIFO)
**GET** `/chamados/pilha`

Lista todos os chamados de emergência aguardando atendimento.

**Response:** (200 OK)
```json
[
  {
    "id": 2,
    "descricao": "Sistema fora do ar",
    "tipo": "EMERGENCIA",
    "dataHoraCriacao": "2025-11-05T00:18:06.088996763",
    "dataHoraAtendimento": null,
    "status": "AGUARDANDO"
  }
]
```

### 4. Listar Todos Chamados em Espera
**GET** `/chamados/em-espera`

Lista todos os chamados (comuns e emergências) aguardando atendimento.

**Response:** (200 OK)
```json
[
  {
    "id": 1,
    "descricao": "Problema no computador",
    "tipo": "COMUM",
    "status": "AGUARDANDO"
  },
  {
    "id": 2,
    "descricao": "Sistema fora do ar",
    "tipo": "EMERGENCIA",
    "status": "AGUARDANDO"
  }
]
```

### 5. Atender Chamado Comum
**DELETE** `/chamados/atender/comum`

Remove e atende o primeiro chamado comum da fila (FIFO).

**Response:** (200 OK)
```json
{
  "id": 1,
  "descricao": "Problema no computador",
  "tipo": "COMUM",
  "dataHoraCriacao": "2025-11-05T00:17:58.137177516",
  "dataHoraAtendimento": "2025-11-05T00:18:42.130988393",
  "status": "ATENDIDO"
}
```

**Response quando não há chamados:** (204 No Content)

### 6. Atender Chamado de Emergência
**DELETE** `/chamados/atender/emergencia`

Remove e atende o último chamado de emergência da pilha (LIFO).

**Response:** (200 OK)
```json
{
  "id": 2,
  "descricao": "Sistema fora do ar",
  "tipo": "EMERGENCIA",
  "dataHoraCriacao": "2025-11-05T00:18:06.088996763",
  "dataHoraAtendimento": "2025-11-05T00:19:14.022759393",
  "status": "ATENDIDO"
}
```

**Response quando não há chamados:** (204 No Content)

### 7. Consultar Histórico Completo
**GET** `/chamados/historico`

Retorna o histórico completo de todos os chamados criados no sistema.

**Response:** (200 OK)
```json
[
  {
    "id": 1,
    "descricao": "Problema no computador",
    "tipo": "COMUM",
    "dataHoraCriacao": "2025-11-05T00:17:58.137177516",
    "dataHoraAtendimento": "2025-11-05T00:18:42.130988393",
    "status": "ATENDIDO"
  },
  {
    "id": 2,
    "descricao": "Sistema fora do ar",
    "tipo": "EMERGENCIA",
    "dataHoraCriacao": "2025-11-05T00:18:06.088996763",
    "dataHoraAtendimento": null,
    "status": "AGUARDANDO"
  }
]
```

## 🧪 Testes

O projeto inclui testes unitários e de integração para garantir o funcionamento correto das funcionalidades.

Execute os testes com:
```bash
mvn test
```

## 📐 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/softwave/chamados/
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── model/           # Entidades do domínio
│   │   ├── service/         # Lógica de negócio
│   │   └── ChamadosApiApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/softwave/chamados/
        ├── controller/      # Testes dos controllers
        └── service/         # Testes dos services
```

## 🔍 Exemplos de Uso

### Criar chamados comuns
```bash
curl -X POST http://localhost:8080/chamados \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Problema no computador","tipo":"COMUM"}'
```

### Criar chamado de emergência
```bash
curl -X POST http://localhost:8080/chamados \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Sistema fora do ar","tipo":"EMERGENCIA"}'
```

### Atender próximo chamado comum
```bash
curl -X DELETE http://localhost:8080/chamados/atender/comum
```

### Visualizar histórico
```bash
curl http://localhost:8080/chamados/historico
```

## 👥 Autores

SoftWave-SPTech

## 📄 Licença

Este projeto é de código aberto para fins educacionais.