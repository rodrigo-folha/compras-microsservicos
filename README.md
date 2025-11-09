# iCompras Microservices: Arquitetura Orientada a Eventos com Spring Boot e Kafka

Este projeto demonstra uma arquitetura de microsserviços orientada a eventos (Event-Driven Architecture - EDA), desenvolvida com **Spring Boot** e **Apache Kafka** para comunicação assíncrona entre os serviços.
Além disso, o sistema utiliza **WebHooks** para integração externa de pagamentos, **MinIO** para armazenamento de arquivos (como Notas Fiscais em PDF) e **JasperReport** para geração automática dos documentos fiscais.

## 🚀 Visão Geral do Projeto

O sistema simula o fluxo de um pedido de compra, desde a criação até a logística de envio, utilizando tópicos do Kafka para desacoplar os serviços e garantir a resiliência e escalabilidade da aplicação.

### Arquitetura

A arquitetura é composta pelos seguintes microserviços e componentes de infraestrutura:

| Componente | Tecnologia Principal | Descrição |
| :--- | :--- | :--- |
| **Clientes** | Spring Boot, JPA, PostgreSQL | Serviço responsável pelo gerenciamento de dados dos clientes. |
| **Produtos** | Spring Boot, JPA, PostgreSQL | Serviço responsável pelo gerenciamento do catálogo de produtos. |
| **Pedidos** | Spring Boot, Kafka | Serviço central que recebe a criação de pedidos e coordena o fluxo de eventos. |
| **Faturamento** | Spring Boot, Kafka, MinIO | Serviço que processa o faturamento do pedido após a confirmação de pagamento. |
| **Logística** | Spring Boot, Kafka | Serviço que gerencia o rastreamento e o envio do pedido. |
| **Apache Kafka** | Docker Compose | Message broker para comunicação assíncrona entre os microserviços. |
| **Zookeeper** | Docker Compose | Gerenciador de coordenação para o cluster Kafka. |
| **Kafka UI** | Docker Compose | Interface web para monitoramento e gerenciamento do Kafka. |
| **PostgreSQL** | Docker Compose | Banco de dados relacional para persistência de dados. |

## 🛠️ Tecnologias Utilizadas

*   **Linguagem:** Java 21
*   **Framework:** Spring Boot 3.5
*   **Mensageria:** Apache Kafka
*   **MinIO:**: Armazenamento da Nota Fiscal gerada
*   **Banco de Dados:** PostgreSQL
*   **Containerização:** Docker e Docker Compose
*   **Build Tool:** Maven

## ⚙️ Pré-requisitos

Para executar o projeto localmente, você precisará ter instalado:

*   **Java Development Kit (JDK) 21** ou superior.
*   **Maven** (opcional, pois o Spring Boot Maven Plugin pode ser usado).
*   **Docker** e **Docker Compose** (essenciais para a infraestrutura).

## 📦 Como Executar o Projeto

Siga os passos abaixo para subir a aplicação completa:

### 1. Clonar o Repositório

```bash
git clone https://github.com/rodrigo-folha/compras-microsservicos.git
cd compras-microsservicos
```

### 2. Subir a Infraestrutura (Kafka, Zookeeper, PostgreSQL, Kafka UI)

Navegue até o diretório de infraestrutura e utilize o Docker Compose:

```bash
cd icompras-servico/broker
docker-compose up -d
```

Aguarde alguns minutos para que todos os serviços estejam completamente inicializados. Você pode verificar o status em:
*   **Kafka UI:** `http://localhost:8090`

### 3. Compilar e Executar os Microserviços

Você pode compilar e executar cada microserviço individualmente. Para simplificar, utilize o Maven Wrapper (`./mvnw`):

```bash
# Navegue para o diretório raiz do projeto
cd ../..

# Compilar todos os projetos
./mvnw clean install

# Executar os microserviços (em terminais separados)
# Microserviço de Clientes
cd clientes/clientes
./mvnw spring-boot:run

# Microserviço de Produtos
cd ../../produtos/produtos
./mvnw spring-boot:run

# Microserviço de Pedidos
cd ../../pedidos/pedidos
./mvnw spring-boot:run

# Microserviço de Faturamento
cd ../../faturamento/faturamento
./mvnw spring-boot:run

# Microserviço de Logística
cd ../../logistica/logistica
./mvnw spring-boot:run
```

## 📝 Fluxo de Eventos Principal

O fluxo de um pedido segue a seguinte sequência de eventos via Kafka:

1.  **Criação do Pedido:** O serviço de **Pedidos** recebe a requisição e publica um evento de "Pagamento Pendente".
2.  **Processamento do Pagamento:** O serviço de **Pedidos** recebe o status do pagamento (simulado) e publica um evento de "Pagamento Publicado".
3.  **Faturamento:** O serviço de **Faturamento** consome o evento de "Pagamento Publicado" e publica um evento de "Faturamento Publicado" e gera uma Nf em pdf armazenando no MinIO.
4.  **Logística:** O serviço de **Logística** consome o evento de "Faturamento Publicado", cadastra o rastreamento e publica um evento de "Envio de Pedido Publicado".
5.  **Atualização do Pedido:** O serviço de **Pedidos** consome os eventos de "Faturamento Publicado" e "Envio de Pedido Publicado" para atualizar o status final do pedido.

## 🔗 Endpoints Principais (Exemplo)

| Serviço | Endpoint | Método | Descrição |
| :--- | :--- | :--- | :--- |
| **Clientes** | `/clientes` | `POST` | Cria um novo cliente. |
| **Clientes** | `/clientes/{id}` | `GET` | Busca um cliente por ID. |
| **Produtos** | `/produtos` | `POST` | Cria um novo produto. |
| **Produtos** | `/produtos/{id}` | `GET` | Busca um produto por ID. |
| **Pedidos** | `/pedidos` | `POST` | Cria um novo pedido, iniciando o fluxo. |
| **Pedidos** | `/pedidos/callback-pagamentos` | `POST` | Realiza Pagamento do Pedido. |
| **Pedidos** | `/pedidos/{id}` | `GET` | Busca um pedido por ID e seu status. |

---