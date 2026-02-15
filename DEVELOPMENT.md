# Development Guide

## Setup Instructions

### 1. Prerequisites

- JDK 17 or higher
- Maven 3.8+
- Docker & Docker Compose
- Git

### 2. Environment Setup

Clone the repository:
```bash
git clone <repository-url>
cd agricultural-price-intelligence
```

### 3. Start Infrastructure

Start all services (PostgreSQL, Elasticsearch, Kafka, Ollama):
```bash
docker-compose up -d
```

Pull the Ollama model (required for AI features):
```bash
docker exec agricultural-ollama ollama pull mistral
```

Verify services are running:
```bash
# Check PostgreSQL
docker exec agricultural-postgres pg_isready -U postgres

# Check Elasticsearch
curl http://localhost:9200/_cluster/health

# Check Kafka
docker exec agricultural-kafka kafka-topics.sh --list --bootstrap-server localhost:9092

# Check Ollama
curl http://localhost:11434/api/tags
```

### 4. Build & Run Application

Build the project:
```bash
mvn clean install
```

Run the application:
```bash
mvn spring-boot:run
```

Or using Java directly:
```bash
java -jar target/agricultural-price-intelligence-1.0.0.jar
```

Application will be available at: `http://localhost:8080`

API Documentation (Swagger): `http://localhost:8080/swagger-ui.html`

### 5. Verify Setup

Test the health endpoint:
```bash
curl http://localhost:8080/api/v1/health
```

Expected response:
```
Agricultural Price Intelligence System is running
```

## Common Development Tasks

### Adding a New Entity

1. Create entity class in `src/main/java/com/myproject/api/entity/`
2. Create corresponding repository in `src/main/java/com/myproject/api/repository/`
3. Create DTO in `src/main/java/com/myproject/api/dto/`
4. Add service methods in `src/main/java/com/myproject/api/service/`
5. Create controller endpoints in `src/main/java/com/myproject/api/controller/`

### Running Tests

Unit tests:
```bash
mvn test
```

Integration tests (requires Docker):
```bash
mvn verify
```

### Database Migrations

New migrations are automatically applied via Hibernate's `ddl-auto: update` setting.

To reset database:
```bash
docker exec agricultural-postgres dropdb -U postgres agricultural_db
docker exec agricultural-postgres createdb -U postgres agricultural_db
```

Then restart the application to recreate schema.

### Publishing to Kafka

Example: Publishing price events via REST API:

```bash
curl -X POST http://localhost:8080/api/v1/prices \
  -H "Content-Type: application/json" \
  -d '{
    "crop": "wheat",
    "region": "punjab",
    "district": "ludhiana",
    "state": "punjab",
    "price": 2500.50,
    "unit": "quintal",
    "market": "ludhiana_mandi",
    "source": "INDIAN",
    "priceDate": "2026-02-15T10:30:00",
    "notes": "Sample price data"
  }'
```

This will:
1. Save to PostgreSQL
2. Publish event to Kafka topic `price-events`
3. Consumer automatically indexes to Elasticsearch

### Consuming Kafka Messages

Monitor messages in real-time:
```bash
docker exec agricultural-kafka kafka-console-consumer.sh \
  --topic price-events \
  --bootstrap-server localhost:9092 \
  --from-beginning
```

### Elasticsearch Queries

Check available indexes:
```bash
curl http://localhost:9200/_cat/indices

# Output example:
# health status index   uuid                   pri rep docs.count docs.deleted store.size pri.store.size
# yellow open   prices  nD4n6L8sS2KXqW7z... 1   1   100      0       50kb       50kb
```

Search for prices:
```bash
curl -X POST http://localhost:9200/prices/_search \
  -H "Content-Type: application/json" \
  -d '{
    "query": {
      "match": {
        "crop": "wheat"
      }
    }
  }'
```

### Using the AI Chat Feature

Get agricultural advice:
```bash
curl "http://localhost:8080/api/v1/chat/advice?crop=wheat&region=punjab&question=how%20to%20improve%20yield"
```

Analyze US import impact:
```bash
curl "http://localhost:8080/api/v1/chat/import-impact?crop=wheat&impactLevel=HIGH"
```

## Troubleshooting

### Port Already in Use

Kill process using port:
```bash
# Linux/Mac
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Cannot Connect to PostgreSQL

```bash
# Check if postgres is running
docker ps | grep postgres

# Check logs
docker logs agricultural-postgres

# Try connection
docker exec -it agricultural-postgres psql -U postgres -c "SELECT 1"
```

### Elasticsearch connection refused

```bash
# Restart Elasticsearch
docker restart agricultural-elasticsearch

# Check health
curl http://localhost:9200/_cluster/health
```

### Kafka broker not available

```bash
# Check Kafka status
docker logs agricultural-kafka

# Verify zookeeper
docker logs agricultural-zookeeper

# Restart both
docker restart agricultural-zookeeper agricultural-kafka
```

### Ollama not responding

```bash
# Check Ollama logs
docker logs agricultural-ollama

# Verify model is downloaded
docker exec agricultural-ollama ollama list

# If not, pull the model
docker exec agricultural-ollama ollama pull mistral
```

## Code Style

This project follows Java conventions:

- Class names: PascalCase (e.g., `PriceService`)
- Method names: camelCase (e.g., `predictPrice`)
- Constants: UPPER_SNAKE_CASE (e.g., `JWT_EXPIRATION`)
- Package names: lowercase with dots (e.g., `com.myproject.api.service`)

## Performance Tips

1. Enable query caching in application.yml for frequently accessed data
2. Use pagination for large result sets
3. Create database indexes for frequently filtered columns
4. Monitor Elasticsearch heap usage with: `curl http://localhost:9200/_nodes/stats`
5. Monitor Kafka consumer lag with Kafka Manager or Conduktor

## Useful Commands

```bash
# View application logs
tail -f application.log

# Check database size
docker exec agricultural-postgres psql -U postgres -d agricultural_db -c "SELECT pg_size_pretty(pg_database_size('agricultural_db'));"

# Rebuild without tests (faster)
mvn clean install -DskipTests

# Run specific test class
mvn test -Dtest=PriceServiceTest

# Generate dependency tree
mvn dependency:tree
```

## Next Steps

- [ ] Implement advanced ML models (LSTM, Prophet)
- [ ] Add WebSocket support for real-time price updates
- [ ] Implement caching layer (Redis)
- [ ] Add GraphQL endpoint
- [ ] Setup CI/CD pipeline (GitHub Actions)
- [ ] Add integration with government agricultural APIs
- [ ] Implement multi-tenant support

