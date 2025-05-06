-- Roles
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_INSTRUCTOR') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_STUDENT') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_ADMINISTRATIVE') ON CONFLICT (name) DO NOTHING;

-- Admin user (password: admin123)
INSERT INTO users (username, password, first_name, last_name, email, active) 
VALUES ('admin', '$2a$10$fEoXCUD2c.KgDOH4pbmKIeYEqHQJRN21xbCGrXmfYsHqe1JQbQlmG', 'Admin', 'User', 'admin@uchk.edu', true)
ON CONFLICT (username) DO NOTHING;

-- User roles
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;

-- Test instructor (password: instructor123)
INSERT INTO users (username, password, first_name, last_name, email, active) 
VALUES ('instructor', '$2a$10$/73uTZQB66NylS49mRcZWeyiZNwOzbgW6Y4OdvKrZVYeGK/QxSUkW', 'John', 'Doe', 'john.doe@uchk.edu', true)
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'instructor' AND r.name = 'ROLE_INSTRUCTOR'
ON CONFLICT DO NOTHING;

-- Test student (password: student123)
INSERT INTO users (username, password, first_name, last_name, email, active) 
VALUES ('student', '$2a$10$6eLPndLnSQJ/j2UYwSNOYeoZBx.47MH5PNicnwWluPjgUEKSjywmq', 'Jane', 'Smith', 'jane.smith@uchk.edu', true)
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'student' AND r.name = 'ROLE_STUDENT'
ON CONFLICT DO NOTHING;

-- Test administrative staff (password: admin123)
INSERT INTO users (username, password, first_name, last_name, email, active) 
VALUES ('staff', '$2a$10$fEoXCUD2c.KgDOH4pbmKIeYEqHQJRN21xbCGrXmfYsHqe1JQbQlmG', 'Sarah', 'Johnson', 'sarah.johnson@uchk.edu', true)
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'staff' AND r.name = 'ROLE_ADMINISTRATIVE'
ON CONFLICT DO NOTHING;

-- Add some instructor profiles
INSERT INTO instructors (first_name, last_name, email, phone_number, type, specialization, user_id)
SELECT u.first_name, u.last_name, u.email, '+221777654321', 'PERMANENT', 'Computer Science', u.id
FROM users u
WHERE u.username = 'instructor'
ON CONFLICT DO NOTHING;

-- Add some student profiles
INSERT INTO students (ine, first_name, last_name, date_of_birth, formation, promotion, start_year, end_year, email, phone_number, employment_status, user_id)
SELECT 'INE123456', u.first_name, u.last_name, '2000-01-01', 'Master Ingénierie Logicielle', '2025', 2023, 2025, u.email, '+221777123456', 'UNKNOWN', u.id
FROM users u
WHERE u.username = 'student'
ON CONFLICT DO NOTHING;

-- Add some document types
INSERT INTO document_types (name) VALUES ('ADMINISTRATIVE') ON CONFLICT (name) DO NOTHING;
INSERT INTO document_types (name) VALUES ('ACADEMIC') ON CONFLICT (name) DO NOTHING;
INSERT INTO document_types (name) VALUES ('RESEARCH') ON CONFLICT (name) DO NOTHING;
INSERT INTO document_types (name) VALUES ('FINANCIAL') ON CONFLICT (name) DO NOTHING;