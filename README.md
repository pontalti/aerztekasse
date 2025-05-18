# 🏥 Aerztekasse

Aerztekasse - code challenge.

---

## 🔧 Development Environment

- **OS:** Fedora 42  
- **JDK:** 21  
- **Build Tool:** Gradle 8.14  
- **Version Control:** Git 2.49.0
- **API Testing Tool:** Postman and curl

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone git@github.com:pontalti/aerztekasse.git
cd aerztekasse
```

Or visit the repo: [https://github.com/pontalti/aerztekasse](https://github.com/pontalti/aerztekasse)

### 2. Requirements

Make sure the following are installed:
- [JDK 21](https://adoptium.net/)
- [Gradle 8.14](https://gradle.org/)
- Your favorite IDE (VSCode, IntelliJ, Eclipse, etc.)

---

## 🛠 Build the Project

Navigate to the project root folder and run:

```bash
./gradlew clean build --refresh-dependencies
```

---

## ▶️ Run the Application

From the project root folder:

### Option 1 – via Gradle:
```bash
./gradlew bootRun
```

### Option 2 – via Jar:
```bash
java -jar build/libs/aerztekasse.jar
```

---

## 🌐 API Overview

The application loads initial data into an **H2 in-memory database** and exposes the following REST endpoints:

### 🏠 Home Endpoint
```bash
curl http://localhost:8080/
```

### 📍 Places

#### Get all places
```bash
curl http://localhost:8080/places
```

#### Get place by ID
```bash
curl http://localhost:8080/places/1
```

#### Get grouped opening hours by place ID
```bash
curl http://localhost:8080/places/1/opening-hours/grouped
```

#### Create one or multiple places
```bash
curl -X POST http://localhost:8080/places \
     -H "Content-Type: application/json" \
     -d @/path/to/postman/places.json
```

#### Delete a place by ID
```bash
curl -X DELETE http://localhost:8080/places/2
```

> 💡 Replace `/path/to/postman/places.json` with the full path to your JSON file.

---

## 🧪 Using Postman

You can import a predefined collection to test all endpoints easily:

```text
<PROJECT_ROOT>/postman/Aerztekasse.postman_collection.json
```

---

## 🧬 OpenAPI Documentation (Swagger)

Access the API documentation via Swagger UI:

```text
http://localhost:8080/swagger-ui/swagger-ui/index.html
```

---

## 🧰 Additional Tools

### Install `curl` (if not already available)

#### Windows
```bash
choco install curl
```

#### Linux (Debian/Ubuntu)
```bash
sudo apt-get install curl
```

#### Linux (Fedora)
```bash
sudo dnf install curl
```
