-- ============================================
-- V2: Datos de prueba
-- ============================================

INSERT INTO sala (nombre, ubicacion, capacidad, descripcion) VALUES
('Sala A - Ejecutiva',  'Piso 1, Ala Norte', 10, 'Sala con proyector y videoconferencia'),
('Sala B - Creativa',   'Piso 2, Ala Sur',     6, 'Ideal para lluvia de ideas'),
('Sala C - Auditorio',  'Piso 1, Central',    30, 'Auditorio para presentaciones grandes');

INSERT INTO responsable (nombre, apellido, email, telefono, cargo) VALUES
('Ana',    'García',    'ana.garcia@empresa.com',    '3001234567', 'Gerente de Proyectos'),
('Carlos', 'Pérez',     'carlos.perez@empresa.com',  '3007654321', 'Jefe de Desarrollo'),
('María',  'Rodríguez', 'maria.rodriguez@empresa.com','3009876543','Coordinadora');

INSERT INTO reserva (sala_id, responsable_id, asunto, descripcion, inicio, fin) VALUES
(1, 1, 'Revisión de sprint', 'Revisión mensual del sprint',     '2025-01-15 09:00:00', '2025-01-15 10:30:00'),
(2, 2, 'Lluvia de ideas UX', 'Sesión de brainstorming',         '2025-01-15 11:00:00', '2025-01-15 12:00:00'),
(3, 3, 'Presentación anual', 'Presentación de resultados 2024', '2025-01-20 14:00:00', '2025-01-20 16:00:00');
