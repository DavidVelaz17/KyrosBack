-- Usuario administrador por defecto para poder acceder al sistema.
-- Contraseña: 1234 (almacenada con BCrypt, como espera Spring Security).
INSERT INTO usuario (nombre_usuario, usuario, password, direccion_usuario, rol) VALUES
    ('Administrador', 'admin', '$2a$10$VCtSgpN1F9JeZdLV28nnL.8R8gBoo5ZSBIiXLKXmhHpLZEer6MXMC', 'N/A', 1);
