-- ============================================
-- V3: Tabla de usuarios para autenticación JWT
-- ============================================

CREATE TABLE usuario (
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(50) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,   -- hash BCrypt
    nombre          VARCHAR(100) NOT NULL,
    rol             VARCHAR(20) NOT NULL DEFAULT 'USER',
    activo          BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_usuario_rol
        CHECK (rol IN ('USER', 'ADMIN'))
);

CREATE INDEX idx_usuario_username ON usuario(username);

-- Usuario de prueba.
-- Password: "admin123" hasheado con BCrypt (coste 10).
-- Puedes regenerar el hash con: new BCryptPasswordEncoder().encode("admin123")
INSERT INTO usuario (username, password, nombre, rol) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrador', 'ADMIN');