#!/bin/sh
set -e

echo "Waiting for Portainer to be ready..."
sleep 10

# Wait for Portainer to be accessible
MAX_RETRIES=30
RETRY_COUNT=0

until curl -sf http://portainer:9000/api/status > /dev/null 2>&1; do
    RETRY_COUNT=$((RETRY_COUNT + 1))
    if [ $RETRY_COUNT -ge $MAX_RETRIES ]; then
        echo "ERROR: Portainer did not become ready in time"
        exit 1
    fi
    echo "Waiting for Portainer API to be ready... (attempt $RETRY_COUNT/$MAX_RETRIES)"
    sleep 2
done

echo "Portainer is ready. Checking if admin user exists..."

# Check if Portainer is already initialized
INIT_STATUS=$(curl -sf http://portainer:9000/api/users/admin/check 2>/dev/null || echo "not-initialized")

if echo "$INIT_STATUS" | grep -q "true"; then
    echo "Admin user already exists. Skipping initialization."
    exit 0
fi

echo "Creating Portainer admin user..."

# Get admin username and password from environment variables
ADMIN_USER="${PORTAINER_ADMIN_USER:-admin}"
ADMIN_PASSWORD="${PORTAINER_ADMIN_PASSWORD:-portainer_admin_password}"

# Initialize admin user using Portainer API
curl -sf -X POST http://portainer:9000/api/users/admin/init \
    -H "Content-Type: application/json" \
    -d "{\"Username\":\"${ADMIN_USER}\",\"Password\":\"${ADMIN_PASSWORD}\"}" \
    > /dev/null 2>&1

if [ $? -eq 0 ]; then
    echo "========================================"
    echo "Portainer admin user created successfully!"
    echo "========================================"
    echo ""
    echo "Admin credentials:"
    echo "  Username: ${ADMIN_USER}"
    echo "  Password: ${ADMIN_PASSWORD}"
    echo ""
    echo "Access Portainer at:"
    echo "  - HTTP:  http://localhost:9002"
    echo "  - HTTPS: https://localhost:9443"
    echo ""
else
    echo "ERROR: Failed to create Portainer admin user"
    exit 1
fi