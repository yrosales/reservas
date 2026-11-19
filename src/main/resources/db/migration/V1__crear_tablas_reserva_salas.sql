-- ============================================
-- V1: Esquema de reserva de salas
-- ============================================

CREATE TABLE sala (
    id              BIGSERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL UNIQUE,
    ubicacion       VARCHAR(200) NOT NULL,
    capacidad       INTEGER NOT NULL CHECK (capacidad > 0),
    descripcion     VARCHAR(500),
    activa          BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE responsable (
    id              BIGSERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    apellido        VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    telefono        VARCHAR(20),
    cargo           VARCHAR(100),
    activo          BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reserva (
    id              BIGSERIAL PRIMARY KEY,
    sala_id         BIGINT NOT NULL,
    responsable_id  BIGINT NOT NULL,
    asunto          VARCHAR(200) NOT NULL,
    descripcion     VARCHAR(500),
    inicio          TIMESTAMP NOT NULL,
    fin             TIMESTAMP NOT NULL,
    estado          VARCHAR(20) NOT NULL DEFAULT 'ACTIVA',
    fecha_creacion  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reserva_sala
        FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reserva_responsable
        FOREIGN KEY (responsable_id) REFERENCES responsable(id) ON DELETE RESTRICT,
    CONSTRAINT chk_reserva_fechas
        CHECK (fin > inicio),
    CONSTRAINT chk_reserva_estado
        CHECK (estado IN ('ACTIVA', 'CANCELADA', 'FINALIZADA'))
);

CREATE INDEX idx_reserva_sala        ON reserva(sala_id);
CREATE INDEX idx_reserva_responsable ON reserva(responsable_id);
CREATE INDEX idx_reserva_inicio_fin  ON reserva(inicio, fin);
CREATE INDEX idx_reserva_estado      ON reserva(estado);

-- Prevención de solapamiento a nivel BD
CREATE EXTENSION IF NOT EXISTS btree_gist;
ALTER TABLE reserva
ADD CONSTRAINT excl_reserva_solapada
EXCLUDE USING gist (
    sala_id WITH =,
    tsrange(inicio, fin, '[)') WITH &&
) WHERE (estado = 'ACTIVA');
