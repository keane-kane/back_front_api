-- Drop tables if they exist
DROP TABLE IF EXISTS document_types CASCADE;
DROP TABLE IF EXISTS instructors CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create user_roles join table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Create instructors table
CREATE TABLE IF NOT EXISTS instructors (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    type VARCHAR(20) NOT NULL,
    specialization VARCHAR(100),
    user_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create students table
CREATE TABLE IF NOT EXISTS students (
    id SERIAL PRIMARY KEY,
    ine VARCHAR(20),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    formation VARCHAR(100),
    promotion VARCHAR(10),
    start_year INTEGER,
    end_year INTEGER,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    employment_status VARCHAR(20),
    user_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create document_types table
CREATE TABLE IF NOT EXISTS document_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);