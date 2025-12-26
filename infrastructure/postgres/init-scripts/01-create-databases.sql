-- Create databases for each microservice
CREATE DATABASE auth_db;
CREATE DATABASE character_db;
CREATE DATABASE campaign_db;
CREATE DATABASE combat_db;
CREATE DATABASE asset_db;
CREATE DATABASE chat_db;
CREATE DATABASE notification_db;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE auth_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE character_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE campaign_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE combat_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE asset_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE chat_db TO dnd_user;
GRANT ALL PRIVILEGES ON DATABASE notification_db TO dnd_user;
