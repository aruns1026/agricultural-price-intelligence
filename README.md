# Agricultural Price Intelligence System

A real-time agricultural price monitoring and prediction system designed to help Indian farmers understand the impact of US agricultural imports on their local markets.

## Features

- **Real-time Price Monitoring**: Track agricultural commodity prices across Indian markets
- **AI-Powered Intelligence**: LLM-based chatbot for agricultural advice and market analysis
- **Price Predictions**: ML-based price forecasting using historical data
- **Event-Driven Architecture**: Kafka-based real-time data streaming
- **Fast Search**: Elasticsearch indexing for rapid price lookups
- **Secure Access**: JWT-based authentication and role-based access control
- **Import Impact Analysis**: Analyze how US agricultural imports affect Indian farmers

## Tech Stack

- **Backend**: Spring Boot 3.3.3 (Java 17)
- **Security**: Spring Security + JWT tokens
- **Database**: PostgreSQL (transactional data)
- **Search**: Elasticsearch 8.11.0
- **Event Streaming**: Apache Kafka
- **AI/LLM**: LangChain4j with Ollama/OpenAI
- **ML**: Apache Commons Math for predictions
- **Documentation**: Swagger/OpenAPI

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- 8GB RAM minimum

### Installation

1. Clone the repository
```bash
git clone <repository-url>
cd agricultural-price-intelligence
```

2. Start infrastructure services
```bash
docker-compose up -d
```

This starts:
- PostgreSQL on port 5432
- Elasticsearch on port 9200
- Kafka on port 9092
- Ollama (LLM) on port 11434

3. Build the application
```bash
mvn clean install
```

4. Run the application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

## Core APIs

### Authentication

**Register User**
```bash
POST /api/v1/auth/register
Content-Type: application/json

{
  "email": "farmer@example.com",
  "password": "secure_password",
  "fullName": "John Doe",
  "phoneNumber": "+919876543210",
  "role": "FARMER"
}
```

**Login**
```bash
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "farmer@example.com",
  "password": "secure_password"
}
```

Response:
```json
{
  "access_token": "eyJhbGciOiJIUzUxMiJ9...",
  "refresh_token": "eyJhbGciOiJIUzUxMiJ9...",
  "email": "farmer@example.com",
  "role": "FARMER"
}
```

### Prices

**Add Price**
```bash
POST /api/v1/prices
Content-Type: application/json

{
  "crop": "wheat",
  "region": "punjab",
  "district": "ludhiana",
  "state": "punjab",
  "price": 2500.50,
  "unit": "quintal",
  "market": "ludhiana_mandi",
  "source": "INDIAN",
  "priceDate": "2026-02-15T10:30:00",
  "notes": "Good quality wheat"
}
```

**Search Prices**
```bash
GET /api/v1/prices/search?crop=wheat&region=punjab&page=0&size=20
```

**Get Price History**
```bash
GET /api/v1/prices/history?crop=wheat&region=punjab&startDate=2026-02-01T00:00:00&endDate=2026-02-15T23:59:59
```

### Price Predictions

**Predict Price**
```bash
POST /api/v1/predictions/predict?crop=wheat&region=punjab
Authorization: Bearer <access_token>
```

**Get Predictions**
```bash
GET /api/v1/predictions?crop=wheat&region=punjab
```

### AI Chat

**Get Agricultural Advice**
```bash
POST /api/v1/chat/advice?crop=wheat&region=punjab&question=how%20to%20improve%20yield
```

**Analyze Import Impact**
```bash
GET /api/v1/chat/import-impact?crop=wheat&impactLevel=HIGH
```

**Market Trend Analysis**
```bash
GET /api/v1/chat/market-trend?crop=wheat&region=punjab
```

## Architecture

### Event Flow

```
Price Data → Kafka Topic (price-events)
             ↓
         Consumer Service
             ↓
     ├→ PostgreSQL (Storage)
     └→ Elasticsearch (Indexing)
```

### Security Flow

```
User Login → JWT Token Generation
         ↓
    JWT Validation Filter
         ↓
    Spring Security Context
         ↓
    Role-Based Authorization
```

## Configuration

### Application Properties

Key configurations in `application.yml`:

```yaml
# Database
spring.datasource.url: jdbc:postgresql://localhost:5432/agricultural_db
spring.datasource.username: postgres
spring.datasource.password: postgres

# Elasticsearch
spring.elasticsearch.rest.uris: http://localhost:9200

# Kafka
spring.kafka.bootstrap-servers: localhost:9092

# JWT
jwt.secret: your-super-secret-jwt-key
jwt.expiration: 86400000  # 24 hours

# LLM
agriculture.llm.type: ollama  # or 'openai'
agriculture.llm.ollama.base-url: http://localhost:11434
agriculture.llm.ollama.model-name: mistral
```

## Database Schema

### Key Tables

- **users**: User accounts with roles (FARMER, ANALYST, ADMIN)
- **prices**: Agricultural commodity prices with timestamps
- **price_alerts**: User-defined price alerts
- **price_predictions**: ML-generated price forecasts
- **import_data**: US agricultural import records

## Deployment

### Docker Deployment

Build Docker image:
```bash
docker build -t agricultural-price-intelligence:1.0.0 .
```

Run container:
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-host:5432/agricultural_db \
  -e JWT_SECRET=your-secret-key \
  agricultural-price-intelligence:1.0.0
```

### Kubernetes Deployment

See `k8s/` directory for deployment manifests.

## Development

### Project Structure

```
src/main/java/com/myproject/api/
├── controller/       # REST endpoints
├── service/         # Business logic
├── entity/          # JPA entities
├── repository/      # Data access layer
├── kafka/           # Event streaming
├── security/        # Authentication & authorization
├── elasticsearch/   # Search indexing
└── dto/            # Data transfer objects
```

### Testing

Run tests:
```bash
mvn test
```

Integration tests use TestContainers for PostgreSQL and Elasticsearch.

## Performance Optimization

- Elasticsearch indexing for sub-second price searches
- Kafka batching for high-throughput data ingestion
- Database connection pooling with HikariCP
- Caching strategies for frequently accessed data

## Monitoring & Logging

- SLF4J logging with configurable levels
- Micrometer metrics integration
- Spring Boot Actuator endpoints for health checks

Access metrics:
```bash
GET http://localhost:8080/actuator/health
GET http://localhost:8080/actuator/metrics
```

## Troubleshooting

### Elasticsearch Connection Issues
```bash
# Check Elasticsearch status
curl http://localhost:9200/_cluster/health

# View Elasticsearch logs
docker logs <elasticsearch-container-id>
```

### Kafka Issues
```bash
# List topics
docker exec <kafka-container> kafka-topics.sh --list --bootstrap-server localhost:9092

# Consume messages
docker exec <kafka-container> kafka-console-consumer.sh --topic price-events --bootstrap-server localhost:9092
```

### Database Connection Problems
```bash
# Check PostgreSQL
docker logs <postgres-container-id>

# Connect to database
docker exec -it <postgres-container> psql -U postgres -d agricultural_db
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to the branch
5. Create a Pull Request

## License

MIT License

## Contact & Support

For issues, questions, or suggestions, please open an issue on GitHub.

## Future Enhancements

- [ ] Mobile app (React Native)
- [ ] WhatsApp/SMS integration for farmers
- [ ] Advanced ML models (LSTM, GRU)
- [ ] Real-time notifications
- [ ] Multi-language support (Hindi, Tamil, Telugu, etc.)
- [ ] Blockchain integration for supply chain transparency
- [ ] Integration with government agricultural databases

