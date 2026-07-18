CREATE TABLE grupo (
    id_grupo        INT AUTO_INCREMENT PRIMARY KEY,
    nombre_grupo    VARCHAR(100)  NOT NULL,
    fecha_inicio    DATE          NOT NULL,
    nombre_plantel  VARCHAR(150)  NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE estudiante (
    id_estudiante         INT AUTO_INCREMENT PRIMARY KEY,
    matricula             VARCHAR(50)   NOT NULL,
    nombre                VARCHAR(150)  NOT NULL,
    apellido_paterno      VARCHAR(100)  NOT NULL,
    apellido_materno      VARCHAR(100)  NOT NULL,
    edad                  SMALLINT      NOT NULL,
    numero_telefonico     INT           NOT NULL,
    escuela_procedencia   VARCHAR(150)  NOT NULL,
    grado_escolar         VARCHAR(50)   NOT NULL,
    nombre_tutor          VARCHAR(150)  NULL,
    telefono_tutor        VARCHAR(20)   NULL,
    direccion             VARCHAR(255)  NOT NULL,
    foto                  VARCHAR(255)  NULL,
    notas                 VARCHAR(500)  NULL,
    fecha_inscripcion     DATE          NOT NULL,
    horario               SMALLINT      NOT NULL,
    ingreso_a             SMALLINT      NOT NULL,
    id_grupo              INT           NULL,
    CONSTRAINT uq_estudiante_matricula UNIQUE (matricula),
    CONSTRAINT fk_estudiante_grupo FOREIGN KEY (id_grupo) REFERENCES grupo (id_grupo) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
