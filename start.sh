#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  D&D Support Platform - Startup Script${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Docker is not running. Please start Docker and try again.${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Docker is running${NC}"
echo ""

# Check if .env file exists
if [ ! -f .env ]; then
    echo -e "${RED}❌ .env file not found!${NC}"
    echo -e "${YELLOW}Creating .env file from template...${NC}"
    cat > .env << 'EOF'
# Database
DB_USER=dnd_user
DB_PASSWORD=dnd_password

# RabbitMQ
RABBITMQ_USER=dnd_user
RABBITMQ_PASSWORD=dnd_password

# MinIO
MINIO_USER=dnd_user
MINIO_PASSWORD=dnd_password

# Grafana
GRAFANA_ADMIN_USER=admin
GRAFANA_ADMIN_PASSWORD=admin

# Portainer
PORTAINER_ADMIN_USER=admin
PORTAINER_ADMIN_PASSWORD=portainer_admin_password

# Timezone
TZ=Europe/Rome
EOF
    echo -e "${GREEN}✓ .env file created${NC}"
fi

echo -e "${GREEN}✓ .env file exists${NC}"
echo ""

# Create necessary directories if they don't exist
echo -e "${YELLOW}Creating necessary directories...${NC}"
mkdir -p infrastructure/postgres/init-scripts
mkdir -p infrastructure/monitoring/grafana/dashboards
mkdir -p infrastructure/monitoring/grafana/datasources
echo -e "${GREEN}✓ Directories created${NC}"
echo ""

# Create PostgreSQL init script if it doesn't exist
if [ ! -f infrastructure/postgres/init-scripts/01-create-databases.sql ]; then
    echo -e "${YELLOW}Creating PostgreSQL init script...${NC}"
    cat > infrastructure/postgres/init-scripts/01-create-databases.sql << 'EOF'
-- Create databases for each microservice
CREATE DATABASE auth_db;
CREATE DATABASE character_db;
CREATE DATABASE campaign_db;
CREATE DATABASE combat_db;
CREATE DATABASE asset_db;
CREATE DATABASE chat_db;
CREATE DATABASE notification_db;
CREATE DATABASE user_db;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE auth_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE character_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE campaign_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE combat_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE asset_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE chat_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE notification_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE user_db TO dnd_user;
EOF
    echo -e "${GREEN}✓ PostgreSQL init script created${NC}"
fi

# Create Prometheus config if it doesn't exist
if [ ! -f infrastructure/monitoring/prometheus.yml ]; then
    echo -e "${YELLOW}Creating Prometheus configuration...${NC}"
    cat > infrastructure/monitoring/prometheus.yml << 'EOF'
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']

  - job_name: 'auth-service'
    metrics_path: '/q/metrics'
    static_configs:
      - targets: ['auth-service:8081']

  - job_name: 'character-service'
    metrics_path: '/q/metrics'
    static_configs:
      - targets: ['character-service:8082']

  - job_name: 'campaign-service'
    metrics_path: '/q/metrics'
    static_configs:
      - targets: ['campaign-service:8083']

  - job_name: 'combat-service'
    metrics_path: '/q/metrics'
    static_configs:
      - targets: ['combat-service:8084']

  - job_name: 'asset-service'
    metrics_path: '/q/metrics'
    static_configs:
      - targets: ['asset-service:8085']

  - job_name: 'chat-service'
    metrics_path: '/q/metrics'
    static_configs:
      - targets: ['chat-service:8086']

  - job_name: 'search-service'
    metrics_path: '/q/metrics'
    static_configs:
      - targets: ['search-service:8087']

  - job_name: 'notification-service'
    metrics_path: '/q/metrics'
    static_configs:
      - targets: ['notification-service:8088']

  - job_name: 'user-service'
    metrics_path: '/q/metrics'
    static_configs:
      - targets: ['user-service:8089']

EOF
    echo -e "${GREEN}✓ Prometheus configuration created${NC}"
fi

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Starting Infrastructure Services${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Stop any existing containers
echo -e "${YELLOW}Stopping any existing containers...${NC}"
docker-compose down -v 2>/dev/null
echo -e "${GREEN}✓ Cleanup complete${NC}"
echo ""

# Start infrastructure services first (without microservices)
echo -e "${YELLOW}Starting infrastructure services (this may take a few minutes)...${NC}"
echo -e "${YELLOW}Services: PostgreSQL, Redis, RabbitMQ, Elasticsearch, MinIO, Vault, Monitoring${NC}"
docker-compose up -d traefik postgres redis rabbitmq elasticsearch minio vault prometheus grafana jaeger portainer

echo ""
echo -e "${YELLOW}Waiting for infrastructure services to be ready...${NC}"
echo -e "${YELLOW}This may take 30-60 seconds...${NC}"

# Wait for PostgreSQL
echo -n "Waiting for PostgreSQL... "
until docker exec dnd-postgres pg_isready -U postgres > /dev/null 2>&1; do
    echo -n "."
    sleep 2
done
echo -e " ${GREEN}✓${NC}"

# Wait for Redis
echo -n "Waiting for Redis... "
until docker exec dnd-redis redis-cli ping > /dev/null 2>&1; do
    echo -n "."
    sleep 2
done
echo -e " ${GREEN}✓${NC}"

# Wait for RabbitMQ
echo -n "Waiting for RabbitMQ... "
until docker exec dnd-rabbitmq rabbitmq-diagnostics ping > /dev/null 2>&1; do
    echo -n "."
    sleep 2
done
echo -e " ${GREEN}✓${NC}"

# Wait for Elasticsearch
echo -n "Waiting for Elasticsearch... "
until curl -s http://localhost:9200/_cluster/health > /dev/null 2>&1; do
    echo -n "."
    sleep 2
done
echo -e " ${GREEN}✓${NC}"

# Wait for MinIO
echo -n "Waiting for MinIO... "
until curl -s http://localhost:9000/minio/health/live > /dev/null 2>&1; do
    echo -n "."
    sleep 2
done
echo -e " ${GREEN}✓${NC}"

# Wait for Vault
echo -n "Waiting for Vault... "
until curl -s http://localhost:8200/v1/sys/health > /dev/null 2>&1; do
    echo -n "."
    sleep 2
done
echo -e " ${GREEN}✓${NC}"

# Initialize Vault secrets
echo -n "Initializing Vault secrets... "
docker-compose up -d vault-init
sleep 5
echo -e " ${GREEN}✓${NC}"

# Wait for Portainer
echo -n "Waiting for Portainer... "
until curl -s http://localhost:9002/api/status > /dev/null 2>&1; do
    echo -n "."
    sleep 2
done
echo -e " ${GREEN}✓${NC}"

# Initialize Portainer admin
echo -n "Initializing Portainer admin... "
docker-compose up -d portainer-init
sleep 5
echo -e " ${GREEN}✓${NC}"

echo ""
echo -e "${GREEN}✓ All infrastructure services are ready!${NC}"
echo ""

# Build multi-module Maven projects
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Building Maven Projects${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Set Maven command - try to use IntelliJ's bundled Maven or system Maven
MAVEN_CMD=""
if [ -x "/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn" ]; then
    MAVEN_CMD="/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn"
    echo -e "${GREEN}✓ Using IntelliJ bundled Maven${NC}"
elif command -v mvn &> /dev/null; then
    MAVEN_CMD="mvn"
    echo -e "${GREEN}✓ Using system Maven${NC}"
else
    echo -e "${YELLOW}⚠ Maven not found. Skipping Maven build step.${NC}"
    echo -e "${YELLOW}  Note: Multi-module projects may fail to build without Maven.${NC}"
fi

# Build multi-module projects if Maven is available
if [ -n "$MAVEN_CMD" ]; then
    # First, build and install the common module
    if [ -d "services/common" ] && [ -f "services/common/pom.xml" ]; then
        echo -e "${YELLOW}Building common module (required by all services)...${NC}"
        cd services/common
        "$MAVEN_CMD" clean install -DskipTests
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ common module built and installed successfully${NC}"
        else
            echo -e "${RED}✗ common module build failed${NC}"
            echo -e "${RED}Cannot continue without common module${NC}"
            cd ../..
            exit 1
        fi
        cd ../..
        echo ""
    fi

    # List of multi-module services
    # Note: user-service must be built before auth-service since auth-service depends on user-service-client
    MULTI_MODULE_SERVICES=("user-service" "auth-service" "character-service" "campaign-service" "combat-service" "asset-service" "chat-service" "notification-service" "search-service")

    for service in "${MULTI_MODULE_SERVICES[@]}"; do
        if [ -d "services/$service" ] && [ -f "services/$service/pom.xml" ]; then
            echo -e "${YELLOW}Building $service multi-module project...${NC}"
            cd services/$service
            "$MAVEN_CMD" clean install -DskipTests
            if [ $? -eq 0 ]; then
                echo -e "${GREEN}✓ $service built successfully${NC}"

                # Create target/classes directories for Quarkus hot-reload support
                # This is needed for empty modules to work with quarkus:dev
                echo -e "${YELLOW}Creating target/classes directories for hot-reload...${NC}"

                # Extract service name without "service" suffix for directory naming
                SERVICE_PREFIX="${service%-service}"

                mkdir -p ${service}-domain/target/classes
                mkdir -p ${service}-view-model/target/classes
                mkdir -p ${service}-adapter-inbound/target/classes
                mkdir -p ${service}-adapter-outbound/target/classes
                mkdir -p ${service}-client/target/classes
                echo -e "${GREEN}✓ Hot-reload directories created${NC}"
            else
                echo -e "${RED}✗ $service build failed${NC}"
                cd ../..
                exit 1
            fi
            cd ../..
            echo ""
        fi
    done
fi

echo ""

# Check if microservices have Dockerfiles
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Checking Microservices${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

SERVICES=("auth-service" "character-service" "campaign-service" "combat-service" "asset-service" "chat-service" "search-service" "notification-service" "user-service" )
MISSING_DOCKERFILES=()

for service in "${SERVICES[@]}"; do
    if [ ! -f "services/$service/Dockerfile" ]; then
        MISSING_DOCKERFILES+=("$service")
        echo -e "${YELLOW}⚠ Missing Dockerfile for $service${NC}"
    else
        echo -e "${GREEN}✓ Dockerfile found for $service${NC}"
    fi
done

echo ""

if [ ${#MISSING_DOCKERFILES[@]} -ne 0 ]; then
    echo -e "${YELLOW}========================================${NC}"
    echo -e "${YELLOW}  Warning: Some Dockerfiles are missing${NC}"
    echo -e "${YELLOW}========================================${NC}"
    echo -e "${YELLOW}The following services don't have Dockerfiles yet:${NC}"
    for service in "${MISSING_DOCKERFILES[@]}"; do
        echo -e "  ${RED}✗${NC} $service"
    done
    echo ""
    echo -e "${YELLOW}Skipping microservices startup for now.${NC}"
    echo -e "${YELLOW}Once you create the Dockerfiles, run:${NC}"
    echo -e "${BLUE}  docker-compose up -d${NC}"
    echo ""
else
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}  Starting Microservices${NC}"
    echo -e "${BLUE}========================================${NC}"
    echo ""
    echo -e "${YELLOW}Building and starting all microservices...${NC}"
    echo -e "${YELLOW}This may take several minutes...${NC}"
    docker-compose up -d --build
    echo ""
    echo -e "${GREEN}✓ All microservices started!${NC}"
    echo ""

    # Wait for Swagger UIs to be available
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}  Waiting for Swagger UIs${NC}"
    echo -e "${BLUE}========================================${NC}"
    echo ""

    SWAGGER_TIMEOUT=120
    SWAGGER_INTERVAL=3
    SWAGGER_FAILED=""

    SWAGGER_SERVICES="auth-service:8081 character-service:8082 campaign-service:8083 combat-service:8084 asset-service:8085 chat-service:8086 search-service:8087 notification-service:8088 user-service:8089"

    for entry in $SWAGGER_SERVICES; do
        service="${entry%%:*}"
        port="${entry##*:}"
        url="http://localhost:${port}/q/swagger-ui/"
        echo -n "Waiting for ${service} Swagger UI (port ${port})... "

        elapsed=0
        while [ $elapsed -lt $SWAGGER_TIMEOUT ]; do
            if curl -s -o /dev/null -w "%{http_code}" "$url" | grep -q "200"; then
                echo -e "${GREEN}✓${NC}"
                break
            fi
            echo -n "."
            sleep $SWAGGER_INTERVAL
            elapsed=$((elapsed + SWAGGER_INTERVAL))
        done

        if [ $elapsed -ge $SWAGGER_TIMEOUT ]; then
            echo -e " ${RED}✗ TIMEOUT${NC}"
            SWAGGER_FAILED="${SWAGGER_FAILED}${service}:${port} "
        fi
    done

    echo ""

    if [ -n "$SWAGGER_FAILED" ]; then
        echo -e "${RED}========================================${NC}"
        echo -e "${RED}  ERROR: Swagger UIs Not Available${NC}"
        echo -e "${RED}========================================${NC}"
        echo -e "${RED}The following services failed to start their Swagger UI:${NC}"
        for failed in $SWAGGER_FAILED; do
            failed_service="${failed%%:*}"
            failed_port="${failed##*:}"
            echo -e "  ${RED}✗${NC} ${failed_service} (port ${failed_port})"
        done
        echo ""
        echo -e "${YELLOW}Troubleshooting tips:${NC}"
        echo -e "  - Check service logs: ${BLUE}docker-compose logs <service-name>${NC}"
        echo -e "  - Verify the service is running: ${BLUE}docker-compose ps${NC}"
        echo -e "  - Check if ports are already in use${NC}"
        echo ""
        exit 1
    fi

    echo -e "${GREEN}✓ All Swagger UIs are available!${NC}"
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  D&D Platform Started Successfully!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${BLUE}Infrastructure Services:${NC}"
echo -e "  🌐 Traefik Dashboard:    ${GREEN}http://localhost:8080${NC}"
echo -e "  🐘 PostgreSQL:           ${GREEN}localhost:5432${NC} (user: dnd_user)"
echo -e "  🔴 Redis:                ${GREEN}localhost:6379${NC}"
echo -e "  🐰 RabbitMQ Management:  ${GREEN}http://localhost:15672${NC} (user: dnd_user, pass: dnd_password)"
echo -e "  🔍 Elasticsearch:        ${GREEN}http://localhost:9200${NC}"
echo -e "  📦 MinIO Console:        ${GREEN}http://localhost:9001${NC} (user: dnd_user, pass: dnd_password)"
echo -e "  🔐 Vault:                ${GREEN}http://localhost:8200${NC} (token: dnd-dev-token)"
echo ""
echo -e "${BLUE}Monitoring Services:${NC}"
echo -e "  📊 Prometheus:           ${GREEN}http://localhost:9090${NC}"
echo -e "  📈 Grafana:              ${GREEN}http://localhost:3000${NC} (user: admin, pass: admin)"
echo -e "  🔎 Jaeger:               ${GREEN}http://localhost:16686${NC}"
echo -e "  🐳 Portainer:            ${GREEN}http://localhost:9002${NC} or ${GREEN}https://localhost:9443${NC} (user: admin, pass: portainer_admin_password)"
echo ""

if [ ${#MISSING_DOCKERFILES[@]} -eq 0 ]; then
    echo -e "${BLUE}Microservices API Endpoints:${NC}"
    echo -e "  🔐 Auth Service:         ${GREEN}http://localhost:8081/q/swagger-ui/${NC}"
    echo -e "  ⚔️ Character Service:   ${GREEN}http://localhost:8082/q/swagger-ui/${NC}"
    echo -e "  🗺️ Campaign Service:    ${GREEN}http://localhost:8083/q/swagger-ui/${NC}"
    echo -e "  ⚔️ Combat Service:      ${GREEN}http://localhost:8084/q/swagger-ui/${NC}"
    echo -e "  📁 Asset Service:        ${GREEN}http://localhost:8085/q/swagger-ui/${NC}"
    echo -e "  💬 Chat Service:         ${GREEN}http://localhost:8086/q/swagger-ui/${NC}"
    echo -e "  🔍 Search Service:       ${GREEN}http://localhost:8087/q/swagger-ui/${NC}"
    echo -e "  🔔 Notification Service: ${GREEN}http://localhost:8088/q/swagger-ui/${NC}"
    echo -e "  👤 User Service:         ${GREEN}http://localhost:8089/q/swagger-ui/${NC}"
    echo ""
fi

echo -e "${GREEN}Happy coding! 🚀${NC}"
echo ""