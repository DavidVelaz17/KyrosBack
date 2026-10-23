-- Catálogo de tipos de egreso (dinero que sale: nómina, papelería, ...). Se administra desde
-- Catálogos en el front, igual que bachilleratos/secundarias.
CREATE TABLE tipo_egreso (
    id_tipo_egreso      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo_egreso  VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- RESTRICT en ambas FK: un tipo con egresos registrados no se puede borrar (rompería el histórico),
-- y siempre se sabe quién registró cada egreso.
CREATE TABLE egreso (
    id_egreso             INT AUTO_INCREMENT PRIMARY KEY,
    id_tipo_egreso        INT           NOT NULL,
    importe_egreso        DOUBLE        NOT NULL,
    fecha_hora_egreso     DATETIME      NOT NULL,
    observaciones_egreso  VARCHAR(500)  NULL,
    id_usuario            INT           NOT NULL,
    CONSTRAINT fk_egreso_tipo_egreso FOREIGN KEY (id_tipo_egreso) REFERENCES tipo_egreso (id_tipo_egreso) ON DELETE RESTRICT,
    CONSTRAINT fk_egreso_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_egreso_fecha_hora ON egreso (fecha_hora_egreso);

INSERT INTO tipo_egreso (nombre_tipo_egreso) VALUES
    ('Nómina'),
    ('Papelería'),
    ('Otro');
