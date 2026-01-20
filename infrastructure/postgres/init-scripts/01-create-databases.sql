-- Create databases for each microservice
CREATE DATABASE auth_db OWNER dnd_user;
CREATE DATABASE character_db OWNER dnd_user;
CREATE DATABASE campaign_db OWNER dnd_user;
CREATE DATABASE combat_db OWNER dnd_user;
CREATE DATABASE asset_db OWNER dnd_user;
CREATE DATABASE chat_db OWNER dnd_user;
CREATE DATABASE notification_db OWNER dnd_user;
CREATE DATABASE user_db OWNER dnd_user;
CREATE DATABASE compendium_db OWNER dnd_user;

-- List all databases (optional, for verification)
SELECT datname FROM pg_database WHERE datistemplate = false;