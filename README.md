# Production-Ready Multi-Module Spring Boot Architecture

This repository demonstrates a battle-tested, decoupled multi-module architecture for Spring Boot (Java 25) applications. The goal of this design is to strictly enforce the **Separation of Concerns (SoC)** principle, making the codebase highly maintainable, easily testable, and resilient to technology stack changes.

By breaking the traditional single-module "monolith-style" package structure into isolated compilation units, we prevent architectural erosion and eliminate tight coupling between business logic and infrastructure.

## Architectural Blueprint & Module Dependencies

The project strictly follows a Contract-Driven, highly decoupled architecture. Dependency flow is strictly controlled to ensure that business logic remains independent of infrastructure and framework specifics.

*Note: The `rest-api` module lives in a completely separate repository with its own independent versioning cycle.*

```text
┌────────────────────────────────────────────────────────────────────────┐
│                           integration-tests                            │ (Integration tests for the APIs)
└───────────────────────────────────┬────────────────────────────────────┘
                                    ▼ (Tests against Docker container)
┌────────────────────────────────────────────────────────────────────────┐
│                              application                               │ (Spring Boot Context, Bootstrapping, Configuration, Docker image build)
└──────┬─────────────────┬───────────────────┬──────────────────────────┬┘
       │                 │                   │                          │
       ▼                 ▼                   ▼                          ▼
┌──────────────┐  ┌──────────────┐  ┌────────────────────────────────┐  │
│   dao-impl   │  │ bridge-impl  │  │         business-logic         │  │
└──────┬───────┘  └──────┬───────┘  └──────┬──────────────┬──────────┘  │
       │                 │                 │              │             │
       ▼                 ▼                 ▼              ▼             │
┌──────────────┐  ┌──────────────┐  ┌──────────────┐┌──────────────┐    │
│   dao-api    │  │  bridge-api  │  │   dao-api    ││  bridge-api  │    │
└──────┬───────┘  └──────────────┘  └──────┬───────┘└──────────────┘    │
       │                                   │                            │
       ▼                                   ▼                            ▼
┌──────────────┐                    ┌──────────────┐             ┌──────────────┐
│    domain    │                    │    domain    │             │   rest-api   │ (Separate Repo)
└──────────────┘                    └──────────────┘             └──────────────┘
```
## Module Breakdown (The Battle-Tested Blueprint)

### 1. `domain`
* **Dependencies:** None.
* **Purpose:** The pure core of the application. Contains domain entities and strict domain-level validations. It remains completely untainted by frameworks or external libraries.

### 2. `dao-api`
* **Dependencies:** `domain` only.
* **Purpose:** Defines the data access contracts (interfaces). It can also house utility enums and helper classes required for data filtering, pagination, or sorting at the abstract layer.

### 3. `dao-impl`
* **Dependencies:** `dao-api`, `spring-data`, `flyway-core`, and in-memory test DBs (`h2`/`hsql`).
* **Purpose:** The concrete database infrastructure adapter. Implements the `dao-api` interfaces using Spring Data and Hibernate, handling data migration and local repository testing isolated from the rest of the application.

### 4. `bridge-api`
* **Dependencies:** None.
* **Purpose:** Defines the communication contracts (SPI) for any external service interaction (e.g., publishing messages, integration with third-party web services).

### 5. `bridge-impl`
* **Dependencies:** `bridge-api`, and specific client libraries (`redisson`, `kafka-clients`, `mqttv3`, etc.).
* **Purpose:** The concrete infrastructure implementation for out-of-process communication. It isolates heavy messaging and caching libraries from the core logic.

### 6. `business-logic`
* **Dependencies:** `dao-api` and `bridge-api`.
* **Purpose:** This is where the core business services, rules, and use-case validations live.

### 7. `application`
* **Dependencies:** `dao-impl`, `bridge-impl`, `business-logic`, database drivers (`mysql-connector-j`), and `spring-boot`.
* **Purpose:** The central Spring Boot context and configuration hub. It acts as a smart proxy—implementing the REST interfaces using Spring Web annotations and delegating the execution directly to the `business-logic` services without violating the architectural contracts. This module is also responsible for building the final Docker image.

### 8. `rest-api` *(Hosted in a Dedicated Repository)*
* **Dependencies:** `jackson` only.
* **Purpose:** Defines the strict API communication contract. Contains purely DTOs (Data Transfer Objects) and interface definitions for the REST endpoints.
* **Why a Separate Repo?** Decoupling the API contract from the backend implementation allows independent versioning. If the backend receives a bug fix, the API contract remains unchanged, ensuring the version doesn't increment unnecessarily. This unblocks Frontend and QA teams to parallelize development based on a stable, early-released contract.

### 9. `integration-tests`
* **Dependencies:** `rest-assured`, `testcontainers`, `spring-kafka`, etc.
* **Purpose:** The ultimate quality gate. It spins up the required infrastructure (Database, Kafka, Redis) using Testcontainers along with a containerized version of the `application` module itself. It executes integration, stress, or load tests acting as a real client, invoking the REST endpoints, and asserting emitted events.

---

## Why Choose This Multi-Module Design?

1. **Strict Boundary Enforcement:** Developers cannot accidentally cross-contaminate layers (e.g., injecting a Spring Data Repository directly into a Web Controller) because the build tool simply won't allow it.
2. **Blazing Fast Local Testing:** You can run pure unit tests in `business-logic` and `dao-impl` in milliseconds, completely bypassing Spring Application Context overhead.
3. **Pluggable Architecture:** Want to swap MySQL (`dao-impl`) for MongoDB, or Kafka (`bridge-impl`) for RabbitMQ? You only rewrite the specific implementation module. The `business-logic` remains untouched.
