SELECT 'CREATE DATABASE product_management_db;'
WHERE NOT EXISTS
(SELECT FROM pg_database WHERE datname = 'product_management_db')\gexec

DO
$user_creation$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'product_manager_user') THEN
    CREATE USER product_manager_user WITH PASSWORD '123456';
  END IF;
END
$user_creation$;

GRANT USAGE, CREATE ON SCHEMA public TO product_manager_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO product_manager_user;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA public TO product_manager_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO product_manager_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, USAGE ON SEQUENCES TO product_manager_user;
