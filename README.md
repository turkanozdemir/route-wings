# ✈️ Route Wings

Route Wings is a full-stack web application that helps users plan and retrieve optimal transportation routes between
locations, taking into account operational days and other conditions.

## 📦 Project Structure

```
route-wings/
├── api/                         # Spring Boot backend application
│   ├── src/                     # Java source code
│   ├── build.gradle             # Backend Gradle build file
│   ├── Dockerfile               # Docker config for backend
│   ├── jacocoTestReport         # Code coverage reports with JaCoCo
│   ├── sonar-project.properties# SonarQube config file
│   └── ...
├── ui/                          # React frontend application
│   ├── public/                  # Static files
│   ├── src/                     # React source files
│   │   ├── components/          # Reusable React components
│   │   ├── services/            # Axios service files for API communication
│   │   ├── App.js               # Root component
│   │   ├── index.js             # Application entry point
│   │   └── ...                  # Styles, test files, utilities
│   ├── Dockerfile               # Docker config for frontend
│   ├── package.json             # NPM configuration
│   └── ...
├── docker-compose.yml           # Orchestrates backend + frontend
├── rt.postman_collection.json   # Postman collection for API testing
└── README.md                    # Project documentation
```

## ⚙️ Tech Stack

**Backend:**

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Lombok
- Swagger / OpenAPI
- Docker
- JaCoCo (Test coverage)
- SonarQube (Static code analysis)

**Frontend:**

- React.js
- Axios
- React Router

**DevOps:**

- Docker & Docker Compose
- Postman (for API testing)
- SonarQube
- JaCoCo

---

## 🚀 Getting Started

### 1. Prerequisites

Make sure you have the following installed:

- Java 17+
- Node.js & npm
- Docker & Docker Compose
- Gradle (optional, project uses Gradle Wrapper)

### 2. Run with Docker

```bash
  docker-compose up --build
```

- The backend will start at: `http://app.routewings.com:8080`
- The frontend will be available at: `http://app.routewings.com:3000`
- Swagger UI: `http://app.routewings.com:8080/swagger-ui/index.html`
- SonarQube UI: `http://sonar.routewings.com:9000`

### 3. Run Manually

#### Backend (Spring Boot)

```bash
  cd api
  ./gradlew bootRun
```

#### Frontend (React)

```bash
  cd ui 
  npm install
  npm start
```

---

## 🛠️ Features

- 🌍 **Location Management** – Add, update, delete, and list airport locations.
- 🚆 **Transportation Management** – Define transport options between locations.
- 🗺️ **Route Calculation** – Calculate optimal routes with filtering by operational days.
- 🔍 **Filtering Support** – Select travel dates and filter transportations dynamically.
- 📦 **RESTful API** – Well-documented API with Swagger UI.
- 📄 **Postman Collection** – Ready-to-test API requests.

---

## ✅ Testing & Quality

### JaCoCo

- JaCoCo is used to measure test coverage.
- Run tests with:

```bash
./gradlew test jacocoTestReport
```

- Coverage report is available under `/build/reports/jacoco/test/html/index.html`.

### SonarQube

- Static code analysis is integrated via SonarQube.
- Configuration in `sonar-project.properties`.
- Run analysis:

```bash
./gradlew sonarqube
```

---

## 🧪 API Testing

Import the `rt.postman_collection.json` file into Postman to test all available routes.

---

## 🐳 Dockerized Setup

The `docker-compose.yml` spins up both the backend and frontend together:

```yml
services:
  api:
    build: ./api
    ports:
      - "8080:8080"
  ui:
    build: ./ui
    ports:
      - "3000:3000"
```

#### Accessing the Application:

Please make sure to add the following entries to your hosts file (`C:\Windows\System32\drivers\etc\hosts`) after
starting the services:

```
    127.0.0.1 		db.routewings.com
    127.0.0.1		app.routewings.com
    127.0.0.1		ui.routewings.com
    127.0.0.1 		sonar.routewings.com
```

---

## 🙋 Author

**Türkan Özdemir**  
📫 [LinkedIn](https://www.linkedin.com/in/turkanozdemir) *(or your real link if you'd like)*

---
