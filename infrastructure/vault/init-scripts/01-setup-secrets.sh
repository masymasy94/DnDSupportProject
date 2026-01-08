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

# Store RabbitMQ credentials
echo "Storing RabbitMQ credentials..."
vault kv put secret/dnd-platform/common/rabbitmq \
    username="dnd_user" \
    password="dnd_password"

# Store MinIO credentials
echo "Storing MinIO credentials..."
vault kv put secret/dnd-platform/common/minio \
    access-key="dnd_user" \
    secret-key="dnd_password"

# Store Redis configuration (for future use if auth is needed)
echo "Storing Redis configuration..."
vault kv put secret/dnd-platform/common/redis \
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

# Store REST client configuration for user-service
echo "Storing REST client configuration for user-service..."
vault kv put secret/dnd-platform/auth-service/rest-client \
    url="http://user-service:8089" \
    auth-token="auth-service-secret-token-12345" \
    auth-header="x-service-token"

echo "========================================"
echo "Vault secrets initialized successfully!"
echo "========================================"
echo ""
echo "Secrets stored at:"
echo "  - secret/dnd-platform/common/database"
echo "  - secret/dnd-platform/common/rabbitmq"
echo "  - secret/dnd-platform/common/minio"
echo "  - secret/dnd-platform/common/redis"
echo "  - secret/dnd-platform/grafana"
echo "  - secret/dnd-platform/portainer"
echo "  - secret/dnd-platform/jwt"
echo "  - secret/dnd-platform/auth-service/jwt-config"
echo "  - secret/dnd-platform/auth-service/rest-client"
