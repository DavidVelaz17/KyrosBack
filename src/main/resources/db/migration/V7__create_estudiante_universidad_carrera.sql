CREATE TABLE estudiante_universidad_carrera (
    id_estudiante_universidad_carrera  INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante_universidad          INT NOT NULL,
    id_carrera                         INT NOT NULL,
    CONSTRAINT uq_estudiante_universidad_carrera UNIQUE (id_estudiante_universidad, id_carrera),
    CONSTRAINT fk_euc_estudiante_universidad FOREIGN KEY (id_estudiante_universidad) REFERENCES estudiante_universidad (id_estudiante_universidad) ON DELETE CASCADE,
    CONSTRAINT fk_euc_carrera FOREIGN KEY (id_carrera) REFERENCES carrera (id_carrera) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
