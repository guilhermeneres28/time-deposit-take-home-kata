# Time Deposit Kata - Kotlin Solution

## Prerequisites

- Java 21+
- Maven 3.6+
- Docker & Docker Compose

## Quick Start

### 1. Start PostgreSQL

```bash
# Start fresh (removes any existing data)
docker-compose down -v
docker-compose up -d

# Wait for PostgreSQL to be ready (5-10 seconds)
sleep 10
```

### 2. Run Tests

```bash
mvn clean test
```

**Expected**: 148 tests passing ✅

### 3. Run Application

```bash
mvn spring-boot:run
```

Application starts at `http://localhost:8080`

## Testing the API

### Using Swagger UI

1. Access: `http://localhost:8080/swagger-ui.html`
2. Navigate to **Time Deposits** section
3. Test the two endpoints:

**Update All Balances**
- `PUT /api/v1/time-deposits/update-balances`
- Click "Try it out" → "Execute"
- Expected: `204 No Content`

**Get All Deposits**
- `GET /api/v1/time-deposits`
- Click "Try it out" → "Execute"
- Expected: `200 OK` with deposits list

### Using cURL

```bash
# Update all balances
curl -X PUT http://localhost:8080/api/v1/time-deposits/update-balances

# Get all deposits
curl http://localhost:8080/api/v1/time-deposits
```

## Cleanup

```bash
# Stop and remove containers
docker-compose down

# Remove volumes (clean database)
docker-compose down -v
```

## Notes

- Database schema is managed by **Flyway** migrations
- Sample data is automatically loaded on first run
- Flyway migrations are located in `src/main/resources/db/migration`
