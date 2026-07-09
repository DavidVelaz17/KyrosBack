CREATE TABLE bachillerato (
    id_bachillerato      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_bachillerato  VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE secundaria (
    id_secundaria      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_secundaria  VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE curso_verano (
    id_curso_verano      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_curso_verano  VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE materia (
    id_materia      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_materia  VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE asesoria (
    id_asesoria     INT AUTO_INCREMENT PRIMARY KEY,
    dia_asesoria    SMALLINT NOT NULL,
    hora_asesoria   SMALLINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE estudiante_bachillerato (
    id_estudiante_bachillerato  INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante               INT NOT NULL,
    id_bachillerato             INT NOT NULL,
    CONSTRAINT uq_estudiante_bachillerato UNIQUE (id_estudiante, id_bachillerato),
    CONSTRAINT fk_estudiante_bachillerato_estudiante FOREIGN KEY (id_estudiante) REFERENCES estudiante (id_estudiante) ON DELETE CASCADE,
    CONSTRAINT fk_estudiante_bachillerato_bachillerato FOREIGN KEY (id_bachillerato) REFERENCES bachillerato (id_bachillerato) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE estudiante_secundaria (
    id_estudiante_secundaria  INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante             INT NOT NULL,
    id_secundaria             INT NOT NULL,
    CONSTRAINT uq_estudiante_secundaria UNIQUE (id_estudiante, id_secundaria),
    CONSTRAINT fk_estudiante_secundaria_estudiante FOREIGN KEY (id_estudiante) REFERENCES estudiante (id_estudiante) ON DELETE CASCADE,
    CONSTRAINT fk_estudiante_secundaria_secundaria FOREIGN KEY (id_secundaria) REFERENCES secundaria (id_secundaria) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE estudiante_curso_verano (
    id_estudiante_curso_verano  INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante               INT NOT NULL,
    id_curso_verano             INT NOT NULL,
    CONSTRAINT uq_estudiante_curso_verano UNIQUE (id_estudiante, id_curso_verano),
    CONSTRAINT fk_estudiante_curso_verano_estudiante FOREIGN KEY (id_estudiante) REFERENCES estudiante (id_estudiante) ON DELETE CASCADE,
    CONSTRAINT fk_estudiante_curso_verano_curso_verano FOREIGN KEY (id_curso_verano) REFERENCES curso_verano (id_curso_verano) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE estudiante_asesoria (
    id_estudiante_asesoria  INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante           INT NOT NULL,
    id_asesoria             INT NOT NULL,
    CONSTRAINT uq_estudiante_asesoria UNIQUE (id_estudiante, id_asesoria),
    CONSTRAINT fk_estudiante_asesoria_estudiante FOREIGN KEY (id_estudiante) REFERENCES estudiante (id_estudiante) ON DELETE CASCADE,
    CONSTRAINT fk_estudiante_asesoria_asesoria FOREIGN KEY (id_asesoria) REFERENCES asesoria (id_asesoria) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE asesoria_materia (
    id_asesoria_materia  INT AUTO_INCREMENT PRIMARY KEY,
    id_asesoria          INT NOT NULL,
    id_materia           INT NOT NULL,
    CONSTRAINT uq_asesoria_materia UNIQUE (id_asesoria, id_materia),
    CONSTRAINT fk_asesoria_materia_asesoria FOREIGN KEY (id_asesoria) REFERENCES asesoria (id_asesoria) ON DELETE CASCADE,
    CONSTRAINT fk_asesoria_materia_materia FOREIGN KEY (id_materia) REFERENCES materia (id_materia) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
