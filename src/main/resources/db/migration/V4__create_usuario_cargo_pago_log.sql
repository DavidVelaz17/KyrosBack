CREATE TABLE usuario (
    id_usuario          INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario      VARCHAR(150) NOT NULL,
    usuario             VARCHAR(100) NOT NULL,
    password            VARCHAR(255) NOT NULL,
    direccion_usuario   VARCHAR(255) NOT NULL,
    rol                 SMALLINT     NOT NULL,
    CONSTRAINT uq_usuario_usuario UNIQUE (usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE cargo (
    id_cargo                  INT AUTO_INCREMENT PRIMARY KEY,
    tipo_mensualidad_cargo    SMALLINT      NOT NULL,
    concepto_cargo            VARCHAR(255)  NULL,
    monto_total_cargo         DOUBLE        NOT NULL,
    fecha_vencimiento_cargo   DATE          NOT NULL,
    estatus_cargo             SMALLINT      NOT NULL,
    id_estudiante             INT           NOT NULL,
    id_usuario                INT           NOT NULL,
    CONSTRAINT fk_cargo_estudiante FOREIGN KEY (id_estudiante) REFERENCES estudiante (id_estudiante) ON DELETE RESTRICT,
    CONSTRAINT fk_cargo_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE pago (
    id_pago             INT AUTO_INCREMENT PRIMARY KEY,
    monto_pagado_pago   DOUBLE    NOT NULL,
    fecha_pago          DATE      NOT NULL,
    metodo_pago         SMALLINT  NOT NULL,
    id_cargo            INT       NOT NULL,
    CONSTRAINT fk_pago_cargo FOREIGN KEY (id_cargo) REFERENCES cargo (id_cargo) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE log (
    id_log       INT AUTO_INCREMENT PRIMARY KEY,
    time_stamp   TIMESTAMP NOT NULL,
    id_usuario   INT       NOT NULL,
    CONSTRAINT fk_log_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
