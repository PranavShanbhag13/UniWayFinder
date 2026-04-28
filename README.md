UniWayFinder
A microservices-based campus navigation and timetable platform that helps university students find their way around campus, manage their class schedules, and stay connected with academic life.

Overview
UniWayFinder is built as a distributed system using Java and Spring Cloud, with each domain capability deployed as an independently scalable microservice. Services discover each other through Nacos, communicate asynchronously via RabbitMQ, and are fronted by a single API gateway that handles routing and JWT-based authentication.

Architecture
The system is organized into two tiers:

Tier 1 — Infrastructure

PostgreSQL — primary relational data store
RabbitMQ — message broker for async events between services
Nacos — service discovery and centralized configuration
Tier 2 — Backend Microservices | Service | Responsibility | | --- | --- | | api-gateway | Single entry point for clients; handles routing, auth, and request forwarding | | identity-service | User registration, authentication, and JWT issuance | | timetable-service | Class schedules and timetable management | | class-location-service | Campus locations, room/building lookups, and wayfinding data | | engagement-service | Student engagement features (e.g. interactions, social features) | | notification-service | Async notifications driven by RabbitMQ events |

              ┌──────────────┐
   Client ─▶  │ api-gateway  │ ─▶ identity / timetable / location / engagement / notification
              └──────────────┘
                     │
              ┌──────┴───────┬───────────────┐
              ▼              ▼               ▼
          PostgreSQL      RabbitMQ         Nacos
Tech Stack
Language: Java
Build: Maven (per service)
Service Discovery & Config: Nacos 2.2.3
Messaging: RabbitMQ 3 (management image)
Database: PostgreSQL 15
Auth: JWT
Containerization: Docker, Docker Compose
CI/CD: GitHub Actions (.github/workflows)
Repository Structure
UniWayFinder/
├── .github/workflows/        # CI/CD pipelines
├── api-gateway/              # Edge gateway service
├── identity-service/         # Auth & user management
├── timetable-service/        # Class timetables
├── class-location-service/   # Campus locations & wayfinding
├── engagement-service/       # Student engagement
├── notification-service/     # Notifications (consumes from RabbitMQ)
├── docker-compose.yml        # Full-stack orchestration
└── .gitignore
Getting Started
Prerequisites
Docker and Docker Compose
Java 17+ and Maven (only if you want to build services from source)
Git
Environment Variables
The Docker Compose file expects the following secrets to be supplied at runtime (via your CI/CD pipeline or a local .env file):

Variable	Description
DB_PASSWORD	PostgreSQL password for the uniwayuser account
MQ_PASSWORD	RabbitMQ password for the admin account
JWT_SECRET	Secret used by services to sign and verify JWTs
Create a .env file in the project root:

DB_PASSWORD=your_db_password
MQ_PASSWORD=your_mq_password
JWT_SECRET=your_jwt_secret
Run the Full Stack
The published Docker images make it possible to spin up the entire system without building anything locally:

git clone https://github.com/PranavShanbhag13/UniWayFinder.git
cd UniWayFinder
docker compose up -d
Once everything is healthy:

API Gateway: http://localhost:8080
Nacos Console: http://localhost:8848/nacos
RabbitMQ Console: http://localhost:15672 (if port is exposed)
Build From Source
Each service is a standalone Maven project. To build one locally:

cd identity-service
mvn clean package
To build a Docker image for a service:

docker build -t uniwayfinder-identity-service:dev ./identity-service
Service Communication
Synchronous calls between services are routed via the API gateway and resolved through Nacos service discovery.
Asynchronous events (e.g. timetable changes triggering notifications) flow through RabbitMQ. The notification-service and timetable-service are the primary publishers/consumers.
Configuration for the prod namespace is centrally managed in Nacos, so services can be reconfigured without rebuilding images.
Development Notes
All services register themselves with Nacos on startup using the NACOS_SERVER and NACOS_NAMESPACE environment variables.
Authentication is centralized: the gateway validates JWTs and downstream services trust the propagated identity.
Database schemas are owned per service — each microservice manages its own tables within the shared PostgreSQL instance.
Roadmap
Frontend client (web/mobile)
Real-time location and indoor navigation
Push notification integration
Observability stack (metrics, tracing, centralized logs)
Contributing
Contributions are welcome. Please open an issue to discuss substantial changes before submitting a pull request.

Authors
Pranav Shanbhag — @PranavShanbhag13
Ian Jun Lai — @ianjunlai
Yashuanjali — @yaswithareddy
Chintalapalli Ganga — @chintalapalliganga02chinni-pixel
Malleshwari K — @malleshwarik06-ai
