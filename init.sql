CREATE USER customer_user WITH PASSWORD 'customer_pass';
CREATE USER account_user WITH PASSWORD 'account_pass';
CREATE USER service_user WITH PASSWORD 'service_pass';

CREATE DATABASE customer;
\c customer;
CREATE SCHEMA customer;
REVOKE ALL ON DATABASE customer FROM PUBLIC;
GRANT ALL PRIVILEGES ON DATABASE customer TO customer_user;
GRANT ALL PRIVILEGES ON SCHEMA customer TO customer_user;

CREATE DATABASE account;
\c account;
CREATE SCHEMA account;
REVOKE ALL ON DATABASE account FROM PUBLIC;
GRANT ALL PRIVILEGES ON DATABASE account  TO account_user;
GRANT ALL PRIVILEGES ON SCHEMA account  TO account_user;

CREATE DATABASE service;
\c service;
CREATE SCHEMA service;
REVOKE ALL ON DATABASE service FROM PUBLIC;
GRANT ALL PRIVILEGES ON DATABASE service  TO service_user;
GRANT ALL PRIVILEGES ON SCHEMA service  TO service_user;
