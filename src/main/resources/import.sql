-- Categories
INSERT INTO categories (name) VALUES ('Software Issues'),('Hardware Issues'),('Network Issues'),('Account Issues');

-- Roles
INSERT INTO roles (name) VALUES ('Admin'),('Operator');

-- Users
INSERT INTO users (username, email, password, is_available) VALUES('Francesco Mosca', 'mosca.f.98@gmail.com', 'PasswordAdmin', FALSE),('Sofia Esposito', 'sofia.esposito@example.com', 'Sofia123', TRUE),('Marco Rossi', 'marco.rossi@example.com', 'Marco123', TRUE),('Luca Bianchi', 'luca.bianchi@example.com', 'Luca123', TRUE),('Laura Verdi', 'laura.verdi@example.com', 'Laura123', TRUE);

-- user roles
INSERT INTO role_user (user_id, role_id) VALUES (1, 1),(2, 2),(3, 2),(4, 2),(5, 2);