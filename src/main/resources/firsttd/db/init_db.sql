-- I used PL/SQL to create the database and the user to not let people create it twice...

DO
&database_creation&
BEGIN
  IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'product_management_db') THEN
    CREATE DATABASE product_management_db;
  END IF;
END
&database_creation&;

DO
&user_creation&
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'product_management_db') THEN
    CREATE USER product_management_db;
  END IF;
END
&user_creation&;
