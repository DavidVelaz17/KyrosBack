CREATE TABLE area (
    id_area      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_area  VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE universidad (
    id_universidad      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_universidad  VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE carrera (
    id_carrera      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_carrera  VARCHAR(150) NOT NULL,
    id_area         INT          NOT NULL,
    CONSTRAINT fk_carrera_area FOREIGN KEY (id_area) REFERENCES area (id_area) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE carrera_universidad (
    id_carrera_universidad  INT AUTO_INCREMENT PRIMARY KEY,
    id_carrera              INT NOT NULL,
    id_universidad          INT NOT NULL,
    CONSTRAINT uq_carrera_universidad UNIQUE (id_carrera, id_universidad),
    CONSTRAINT fk_carrera_universidad_carrera FOREIGN KEY (id_carrera) REFERENCES carrera (id_carrera) ON DELETE CASCADE,
    CONSTRAINT fk_carrera_universidad_universidad FOREIGN KEY (id_universidad) REFERENCES universidad (id_universidad) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE estudiante_universidad (
    id_estudiante_universidad  INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante              INT NOT NULL,
    id_universidad             INT NOT NULL,
    CONSTRAINT uq_estudiante_universidad UNIQUE (id_estudiante, id_universidad),
    CONSTRAINT fk_estudiante_universidad_estudiante FOREIGN KEY (id_estudiante) REFERENCES estudiante (id_estudiante) ON DELETE CASCADE,
    CONSTRAINT fk_estudiante_universidad_universidad FOREIGN KEY (id_universidad) REFERENCES universidad (id_universidad) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
