#!/bin/sh
set -e

export VAULT_ADDR='http://vault:8200'
export VAULT_TOKEN='dnd-dev-token'

echo "Waiting for Vault to be ready..."
sleep 5

# Check if Vault is ready
until vault status > /dev/null 2>&1; do
    echo "Vault is not ready yet, waiting..."
    sleep 2
done

echo "Vault is ready. Setting up secrets..."

# Enable KV v2 secrets engine (already enabled in dev mode, but this is idempotent)
vault secrets enable -path=secret kv-v2 2>/dev/null || echo "KV v2 already enabled"

# Pre-generated RSA keys for development (DO NOT USE IN PRODUCTION)
# These are dev-only keys - in production, use proper key management
PRIVATE_KEY='-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDDl72s95zEZncV
dnguU15+9ftytLXKa1UQ9I0j4wRlTqG/p1yc3xgXOjqTqKi8vb9sLTeosKEV4sDX
ibPhOMNUooA/dz+UQ4O7lU2cQ22b0elcLKrK9hZ+AP4z+l/wl4WFgkszFO05yeGG
yooPnoSMUyZ7zxqabawWI2+eeY0UgrgHm3SAFE19r20f3hEgnoPIpZ2GYWt0a4HH
JTW9i7dQLruehyEvy2pvS+8WKGJdKyknHlOQmTymhz+EkNCTS8SWAI0mnRfvcP9U
wbZTHWXhx/5c13DEMaOCL8XCOjxZEm/2A9v89ylap5sDZnnvrjO8m+WzFW346BiS
y6m/lWgNAgMBAAECggEAKXjbNvmpu9seFObxxKDYwILXthKrGSh4+QdhheJ05CYw
Fbse4ARNz18BXJ2+aYayN/W+f340KbfSu+DuyyqGAEQUfWCDkqReIkAeHKwPaaKw
oGK4NCDJ1B3RyKOmBBWcshpWZ5ZgVoh/0VzQuU8bO8XFuyrGsUff3THWUPvk9hwC
fHeIlzAYE2jvolGhzYEV1fXLrXXTgqAYrijg10+SZkwHgSErOQ9YqL+jH6vgqnst
O15Phjkng111fIuckqp3SLbRWX0lGkjSPzSJaRSPYqZRuorSZF7I7QvmLqOfdF64
XESFZ7i0oPC/jPXy1mNE/ku37gfdG5yFcj4uhgn14QKBgQDp+RCRj4XekYNTrEer
VoIrXdvpGbNzsRpzPfL/s1wQJ6dZ78becxMNORlDk9+RPXa+xusBgovNU5Zxgby3
vK6ymRLkT8s9doRqloF7NpubvmjSSK2zMxZqbdvJ9WpS3wcH6vDtZqmm6rsK1lCB
LRtXN4j4Zyp79F2MTxt6z7vNzwKBgQDWAa7my40e1JKexVxA9dRctXFkx3AyjzmP
99mnXzeUevGJO6Jo3iDTQwO7SpfOgFDKpF9Vgi0EGFbGwKJowBUAidvvk36b/ld+
7iwHRv1u7Jana5+N1JW++kzZj52wvECGDcjEzN5N8Hz8sL4i+nc9LDC7Ae/ASVaA
yp84cFtfYwKBgBYWkEzdmsq/PAGrMvzm5tUHQFqKNZnHb3LPt2ASY6+WvsLL0x9v
B65o5IyvqhyBC/PIvajf1SjmKj6++7zw0CFGODVbnxlHVgrl+IVUS25xlF29xMA1
I3mas3QmegomyfgiYupWlc+PO2czglgflQQv1vt0adtX33j+gt6eO+GbAoGBAJKM
nogf1ijJqwoc3tsBPQarndZEmPmWiPWk5aCvHLGKK4Mtmj8z9ciWOKT2JNTdm/XY
8AvvVAJc5HfAjLcs6nTu8vSxzV7nomUilnX5UC24yEYn9iA69rzMTPojVfokQA9M
uzLBlG5NBFpKcjojyKnUbB5z6gmEbz9NAhG1J20/AoGBAJ822cVYlT0r+9rpIhXV
CbCKH4UGFraTe2V+3ulM6gDi733yJAYi4qPEZf1ifWujdn4nrK8JJzPpMSxuX9Xt
EIqjALUSLir6NM2gFwPST4elZnfFA1Sa1ax2xRrHCqrhXus7V1aZnRbtAqajtTWF
aI5WEjglHYrP0eTfGqSY0lAN
-----END PRIVATE KEY-----'

PUBLIC_KEY='-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw5e9rPecxGZ3FXZ4LlNe
fvX7crS1ymtVEPSNI+MEZU6hv6dcnN8YFzo6k6iovL2/bC03qLChFeLA14mz4TjD
VKKAP3c/lEODu5VNnENtm9HpXCyqyvYWfgD+M/pf8JeFhYJLMxTtOcnhhsqKD56E
jFMme88amm2sFiNvnnmNFIK4B5t0gBRNfa9tH94RIJ6DyKWdhmFrdGuBxyU1vYu3
UC67nochL8tqb0vvFihiXSspJx5TkJk8poc/hJDQk0vElgCNJp0X73D/VMG2Ux1l
4cf+XNdwxDGjgi/Fwjo8WRJv9gPb/PcpWqebA2Z5764zvJvlsxVt+OgYksupv5Vo
DQIDAQAB
-----END PUBLIC KEY-----'

# Store common database credentials (keys match application.properties placeholders)
echo "Storing database credentials..."
vault kv put secret/dnd-platform/common/database \
    username="dnd_user" \
    password="dnd_password"

# Store auth-service specific database configuration
echo "Storing auth-service database configuration..."
vault kv put secret/dnd-platform/auth-service/database \
    url="jdbc:postgresql://postgres:5432/auth_db"

# Store user-service specific database configuration
echo "Storing user-service database configuration..."
vault kv put secret/dnd-platform/user-service/database \
    url="jdbc:postgresql://postgres:5432/user_db"

# Store character-service specific database configuration
echo "Storing character-service database configuration..."
vault kv put secret/dnd-platform/character-service/database \
    url="jdbc:postgresql://postgres:5432/character_db" \
    auth-service-url="http://auth-service:8081" \
    search-service-url="http://search-service:8087"

# Store campaign-service specific database configuration
echo "Storing campaign-service database configuration..."
vault kv put secret/dnd-platform/campaign-service/database \
    url="jdbc:postgresql://postgres:5432/campaign_db" \
    auth-service-url="http://auth-service:8081" \
    character-service-url="http://character-service:8082" \
    asset-service-url="http://asset-service:8085"

# Store combat-service specific database configuration
echo "Storing combat-service database configuration..."
vault kv put secret/dnd-platform/combat-service/database \
    url="jdbc:postgresql://postgres:5432/combat_db" \
    auth-service-url="http://auth-service:8081" \
    character-service-url="http://character-service:8082"

# Store chat-service specific database configuration
echo "Storing chat-service database configuration..."
vault kv put secret/dnd-platform/chat-service/database \
    url="jdbc:postgresql://postgres:5432/chat_db" \
    auth-service-url="http://auth-service:8081"

# Store asset-service specific database configuration
echo "Storing asset-service database configuration..."
vault kv put secret/dnd-platform/asset-service/database \
    url="jdbc:postgresql://postgres:5432/asset_db"

# Store notification-service specific database configuration
echo "Storing notification-service database configuration..."
vault kv put secret/dnd-platform/notification-service/database \
    url="jdbc:postgresql://postgres:5432/notification_db"

# Store search-service specific configuration
echo "Storing search-service configuration..."
vault kv put secret/dnd-platform/search-service/config \
    elasticsearch-scheme="http" \
    elasticsearch-host="elasticsearch" \
    elasticsearch-port="9200" \
    auth-service-url="http://auth-service:8081"

# Store RabbitMQ credentials and configuration
echo "Storing RabbitMQ credentials and configuration..."
vault kv put secret/dnd-platform/common/rabbitmq \
    username="dnd_user" \
    password="dnd_password" \
    host="rabbitmq" \
    port="5672" \
    virtual-host="dnd_vhost"

# Store MinIO credentials
echo "Storing MinIO credentials..."
vault kv put secret/dnd-platform/common/minio \
    access-key="dnd_user" \
    secret-key="dnd_password"

# Store Redis configuration
echo "Storing Redis configuration..."
vault kv put secret/dnd-platform/common/redis \
    hosts="redis://redis:6379" \
    password=""

# Store Grafana admin credentials
echo "Storing Grafana credentials..."
vault kv put secret/dnd-platform/grafana \
    admin-user="admin" \
    admin-password="admin"

# Store Portainer admin credentials
echo "Storing Portainer credentials..."
vault kv put secret/dnd-platform/portainer \
    admin-user="admin" \
    admin-password="portainer_admin_password"

# Store JWT keys (without prefix - Quarkus Vault adds secret path prefix automatically)
echo "Storing JWT keys..."
vault kv put secret/dnd-platform/jwt \
    private-key="$PRIVATE_KEY" \
    public-key="$PUBLIC_KEY"

# Store JWT configuration for auth-service
echo "Storing JWT configuration..."
vault kv put secret/dnd-platform/auth-service/jwt-config \
    issuer="dnd-platform" \
    access-token-expiry-seconds="3600" \
    refresh-token-expiry-days="30"

# Store service-to-service authentication tokens
echo "Storing service-to-service authentication tokens..."
vault kv put secret/dnd-platform/common/service-token \
    rest_client_user_service_auth="dev-service-token-change-in-production" \
    rest_client_user_service_auth_http_header="x-service-token"

# Store REST client URLs for service-to-service communication
echo "Storing REST client URLs..."
vault kv put secret/dnd-platform/auth-service/rest-client \
    url="http://user-service:8089"

# Store Vault connection configuration
echo "Storing Vault configuration..."
vault kv put secret/dnd-platform/common/vault \
    url="http://vault:8200" \
    token="dnd-dev-token"

echo "========================================"
echo "Vault secrets initialized successfully!"
echo "========================================"
echo ""
echo "Secrets stored at:"
echo "  - secret/dnd-platform/common/database"
echo "  - secret/dnd-platform/common/rabbitmq"
echo "  - secret/dnd-platform/common/minio"
echo "  - secret/dnd-platform/common/redis"
echo "  - secret/dnd-platform/common/service-token"
echo "  - secret/dnd-platform/common/vault"
echo "  - secret/dnd-platform/grafana"
echo "  - secret/dnd-platform/portainer"
echo "  - secret/dnd-platform/jwt"
echo "  - secret/dnd-platform/auth-service/jwt-config"
echo "  - secret/dnd-platform/auth-service/database"
echo "  - secret/dnd-platform/auth-service/rest-client"
echo "  - secret/dnd-platform/user-service/database"
echo "  - secret/dnd-platform/character-service/database"
echo "  - secret/dnd-platform/campaign-service/database"
echo "  - secret/dnd-platform/combat-service/database"
echo "  - secret/dnd-platform/chat-service/database"
echo "  - secret/dnd-platform/asset-service/database"
echo "  - secret/dnd-platform/notification-service/database"
echo "  - secret/dnd-platform/search-service/config"
