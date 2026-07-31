-- Indica si el alumno lleva inglés (aplica principalmente a alumnos que ingresan a universidad).
ALTER TABLE estudiante ADD COLUMN lleva_ingles BOOLEAN NOT NULL DEFAULT FALSE;
