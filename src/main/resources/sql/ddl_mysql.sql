DROP DATABASE IF EXISTS inquiry;
CREATE DATABASE inquiry;
USE inquiry;

CREATE TABLE IF NOT EXISTS inquiry (
  id BIGINT PRIMARY KEY,
  name  VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  detail VARCHAR(10200) NOT NULL
);
